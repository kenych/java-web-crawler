# Dockerfile

FROM openjdk:8

MAINTAINER  kayan.subscriber@gmail.com

WORKDIR /home/crawler

COPY files/crawler-1.0-SNAPSHOT.jar /home/crawler/lib/

COPY files/entrypoint.sh /home/crawler/

RUN groupadd -r crawler && useradd -r -m -g crawler crawler

RUN chown -R crawler:crawler /home/crawler && chmod 755 /home/crawler/entrypoint.sh

USER crawler

ENTRYPOINT ["/home/crawler/entrypoint.sh"]

