# Pull base image
FROM openjdk:8u181


COPY target/universal/lunchrooms-ms-1.0-SNAPSHOT/ /root/lunchrooms/
WORKDIR /root/lunchrooms

EXPOSE 9000

