# Dockerfile

FROM openjdk:8

MAINTAINER  kayan.subscriber@gmail.com

ADD https://download.elastic.co/beats/filebeat/filebeat_1.0.1_amd64.deb /tmp

USER root

RUN  dpkg -i /tmp/filebeat_1.0.1_amd64.deb &&  apt-get install -f .

WORKDIR /home/crawler

COPY entrypoint.sh /home/crawler/
COPY target/crawler-1.0-SNAPSHOT.jar /home/crawler/lib/

RUN chmod 755 /home/crawler/entrypoint.sh

ENTRYPOINT ["/home/crawler/entrypoint.sh"]

