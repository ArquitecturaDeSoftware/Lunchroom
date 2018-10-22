# Pull base image
FROM openjdk:8u181
FROM hseeberger/scala-sbt
FROM garthk/unzip

COPY target/universal/lunchrooms-ms-1.0-SNAPSHOT.zip/ /root/lunchrooms/
RUN cd /root/lunchrooms/
RUN unzip lunchrooms-ms-1.0-SNAPSHOT.zip 
RUN rm -rf lunchrooms-ms-1.0-SNAPSHOT.zip 
WORKDIR /root/lunchrooms

EXPOSE 9000

