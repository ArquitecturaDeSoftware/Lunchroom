# Pull base image
FROM openjdk:8u181

RUN sbt dist
COPY target/universal/lunchrooms-ms-1.0-SNAPSHOT/ /root/lunchrooms/
WORKDIR /root/lunchrooms

EXPOSE 9000

