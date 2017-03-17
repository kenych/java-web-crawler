package crawler;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static java.nio.charset.Charset.defaultCharset;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class CrawlerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8081);

    @Before
    public void setup() throws IOException {
        givenPageExists("crawl.html");
        givenPageExists("crawl2.html");
    }

    private void givenPageExists(String page) throws IOException {
        givenThat(get(urlMatching("/" + page)).willReturn(aResponse()
                .withStatus(200)
                .withBody(Resources.toString(Resources.getResource(page), defaultCharset()))
                .withHeader("Content-Type", "text/html")));
    }

    @Test
    public void testVisitSinglePageWithNoLinks() throws IOException, InterruptedException {
        Page page = new Crawler("http://localhost:8081", 1).visit("http://localhost:8081/crawl2.html");

        System.out.println(page);

        assertThat(page.getLink(), is("http://localhost:8081/crawl2.html"));
        assertThat(page.getError().isPresent(), is(false));
        assertThat(page.getLinks().size(), is(0));
        assertThat(page.getExternalLinks().size(), is(0));
        assertThat(page.getImages().size(), is(3));
        assertThat(page.getScripts().size(), is(2));
        assertThat(page.getImports().size(), is(1));
    }

    @Test
    public void testVisitPageWithTwoLinksAndOneError() throws IOException, InterruptedException, ExecutionException {
        List<Page> pages = new Crawler("http://localhost:8081", 2).crawl("http://localhost:8081/crawl.html");

        System.out.println(pages);

        assertThat(pages.get(0).getLink(), is("http://localhost:8081/crawl.html"));
        assertThat(pages.get(0).getError().isPresent(), is(false));
        assertThat(pages.get(0).getLinks().size(), is(2));
        assertThat(pages.get(0).getExternalLinks().size(), is(1));
        assertThat(pages.get(0).getImages().size(), is(1));
        assertThat(pages.get(0).getScripts().size(), is(0));
        assertThat(pages.get(0).getImports().size(), is(0));

        assertThat(pages.get(1).getLink(), is("http://localhost:8081/crawl2.html"));
        assertThat(pages.get(1).getError().isPresent(), is(false));
        assertThat(pages.get(1).getLinks().size(), is(0));
        assertThat(pages.get(1).getExternalLinks().size(), is(0));
        assertThat(pages.get(1).getImages().size(), is(3));
        assertThat(pages.get(1).getScripts().size(), is(2));
        assertThat(pages.get(1).getImports().size(), is(1));

        assertThat(pages.get(2).getLink(), is("http://localhost:8081/crawl3.html"));
        assertThat(pages.get(2).getError().isPresent(), is(true));
        assertThat(pages.get(2).getLinks().size(), is(0));
        assertThat(pages.get(2).getExternalLinks().size(), is(0));
        assertThat(pages.get(2).getImages().size(), is(0));
        assertThat(pages.get(2).getScripts().size(), is(0));
        assertThat(pages.get(2).getImports().size(), is(0));
    }

    @Test
    @Ignore(value = "just run this manually with some web site")
    public void testSomeUrl() throws IOException, InterruptedException, ExecutionException {
        List<Page> pages = new Crawler("http://www.mkyong.com", 10).crawl("http://www.mkyong.com");
        pages.forEach(page -> {
            System.out.println("=== new page ====\n\n\n\n" + page);
        });
    }
}