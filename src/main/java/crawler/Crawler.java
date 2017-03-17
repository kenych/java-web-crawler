package crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Crawler {

    private static final String ERROR_MESSAGE = "Please enter start page, domain, number_of_threads. Example:\n" +
            "java -jar crawler-1.0-SNAPSHOT.jar http://www.example.com http://www.example.com 5";
    private ExecutorService executor;
    private AtomicInteger counter = new AtomicInteger();
    private List<Page> pages = new LinkedList<>();
    private LinkedBlockingQueue<Future<Page>> queue = new LinkedBlockingQueue();

    //no concurrent hash set, use map instead
    private ConcurrentMap<String, String> set = new ConcurrentHashMap<>();
    private String domain;

    public Crawler(String domain, int numberOfthreads) {
        executor = Executors.newFixedThreadPool(numberOfthreads);
        this.domain = domain;
    }

    public static void main(String[] args) throws Exception {
        try {
            String domain = args[0];
            String startPage = args[1];
            int threads = Integer.valueOf(args[2]);

            System.out.println("crawling to: " + startPage + " with domain: " + domain + " with number of threads: " + threads);

            List<Page> pages = new Crawler(domain, threads).crawl(startPage);

            pages.forEach(page -> {
                System.out.println("=== new page ====\n\n\n\n" + page);
            });
        } catch (Exception e) {
            System.out.println("Error: " + e);
            System.out.println(ERROR_MESSAGE);
        }
    }

    public List<Page> crawl(String link) throws InterruptedException, ExecutionException {
        visitIfNotYetInAThread(link);

        //wait so queue is ready
        Thread.sleep(1000);

        while (!queue.isEmpty()) {
            pages.add(queue.poll().get());

            System.out.println("jobs: " + queue.size());
            System.out.println("processed link: " + counter.get());
        }

        executor.shutdown();
        System.out.println("processed links: " + counter.get());

        return pages;
    }

    private void visitIfNotYetInAThread(String link) {
        if (set.putIfAbsent(link, "") == null) {
            try {
                Future<Page> submit = executor.submit(() -> {
                    counter.incrementAndGet();
                    try {
                        return visit(link);
                    } catch (IOException e) {
                        /*
                          ideally there should be a retry mechanism with retry policy:
                          something like this one I written a bti earlier:
                          https://bitbucket.org/kenych/demo/src/53156c72faa00c9e228e9f93de5471ef4db510b2/newTvPrograms/src/main/java/tvprogram/infrastructure/retry/?at=master
                          for sake of test it is skipped, so
                          if request timeout or too many requests happen we just get error on the page.
                          Also if link is not a page but some other resource, like zip file etc, this is treated as error, although
                          could be improved.
                         */
                        return new Page(link, e.getMessage());
                    }
                });
                queue.put(submit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Page visit(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();

        Elements img = doc.getElementsByTag("img");
        Elements scripts = doc.getElementsByTag("script");
        Elements imports = doc.select("link[href]");
        Elements linksOnSameDomain = doc.select("a[href^=\"" + domain + "/\"]");
        Elements externalLinks = doc.select("a[href~=^((?!" + domain + ").)*$]");

        Page page = new Page(link, linksOnSameDomain, externalLinks, img, scripts, imports);
        linksOnSameDomain.forEach(element -> visitIfNotYetInAThread(element.attr("abs:href")));

        return page;
    }

}
