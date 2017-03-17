This is pretty basic example of web page crawling in java and is not fully production ready crawler and is done for test purposes only.

Running code against different web sites showed
there are some issues which could be improved.
For example it can't follow relative links at current stage, also some web sites
 have dynamically generated links, which actually might refer to same page, so if this happens
 then application will run in a loop and will never finish.
There could be other issues as well, in fact as it is crawling external resources which could be very various,
it is hard to cover all cases.

Other issues could be regarding retry mechanism which is missing, better error handling, etc etc.
But for the sake of this test I have skipped them.

Code is covered by tests and example web server with two pages(crawl.html, crawl2.html)

The code is written in concurrent way and using given number of threads.

To test the application either run CrawlerTest tests or commands below:
mvn install
cd target/
java -jar crawler-1.0-SNAPSHOT.jar http://www.example.com http://www.example.com 5


Running as docker container:
go to src/main/container, then
cp ../../../target/crawler-1.0-SNAPSHOT.jar files
docker build  -t web-crawler:current .
docker run  -t web-crawler:current  http://www.example.com http://www.example.com 5

With regards,
Kayan A.











