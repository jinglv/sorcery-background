FROM java:8
MAINTAINER lvjing
VOLUME /tmp
ADD target/sorcery-background-0.0.1-SNAPSHOT.jar sorcery.jar
ENTRYPOINT ["java","-jar","sorcery.jar"]