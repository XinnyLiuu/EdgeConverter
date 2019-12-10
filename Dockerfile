FROM ubuntu:18.04

USER root

# Install maven + java 8
RUN apt-get update && \
    apt-get -y install wget && \
    wget https://d3pxv6yz143wms.cloudfront.net/8.212.04.1/java-1.8.0-amazon-corretto-jdk_8.212.04-1_amd64.deb && \
    apt-get -y install java-common && \
    dpkg --install java-1.8.0-amazon-corretto-jdk_8.212.04-1_amd64.deb && \
    apt-get -y install maven

# Shares linux X11 socket with docker container
# http://fabiorehm.com/blog/2014/09/11/running-gui-apps-with-docker/
RUN export uid=1000 gid=1000 && \
    mkdir -p /home/developer && \
    echo "developer:x:${uid}:${gid}:Developer,,,:/home/developer:/bin/bash" >> /etc/passwd && \
    echo "developer:x:${uid}:" >> /etc/group && \
    chown ${uid}:${gid} -R /home/developer

# Setup edgeconvert.EdgeConvertGUI
WORKDIR /edgeconvert

COPY . /edgeconvert

RUN mvn package

WORKDIR /edgeconvert/target

USER developer

CMD ["java", "-jar", "EdgeConverter-1.0-SNAPSHOT-jar-with-dependencies.jar", "-Djava.awt.headless=true"]
