FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/KebunJio-0.0.1-SNAPSHOT.jar /app/KebunJio.jar

ENTRYPOINT ["java", "-jar", "KebunJio.jar"]

EXPOSE 8080
