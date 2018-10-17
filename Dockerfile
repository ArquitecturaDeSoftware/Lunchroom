# Pull base image
FROM openjdk:8u181

RUN echo "deb https://dl.bintray.com/sbt/debian /"
#RUN sudo tee -a /etc/apt/sources.list.d/sbt.list
RUN sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
RUN sudo apt-get update
RUN sudo apt-get install sbt

RUN sbt dist
COPY target/universal/lunchrooms-ms-1.0-SNAPSHOT/ /root/lunchrooms/
WORKDIR /root/lunchrooms

EXPOSE 9000

