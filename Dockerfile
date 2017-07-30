FROM openjdk:8

MAINTAINER Kayan Azimov kayan.subscriber@gmail.com

COPY target/crawler-1.0-SNAPSHOT.jar /

ENTRYPOINT ["java", "-jar", "crawler-1.0-SNAPSHOT.jar"]