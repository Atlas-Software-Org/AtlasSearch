FROM eclipse-temurin:24-noble

# RUN apt update && apt install -y openjdk-23-jdk

COPY backend/target/backend-1.0-SNAPSHOT.jar /backend.jar
COPY frontend/dist /frontend

WORKDIR /srv
ENTRYPOINT [ "java", "-jar", "/backend.jar" ]
