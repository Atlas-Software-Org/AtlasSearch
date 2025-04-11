FROM eclipse-temurin:24-noble

# RUN apt update && apt install -y openjdk-23-jdk

COPY target/crawler-1.0-SNAPSHOT.jar /crawler.jar

WORKDIR /srv
ENTRYPOINT [ "java", "-jar", "/crawler.jar" ]
