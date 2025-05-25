FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/KebunJio-0.0.1-SNAPSHOT.jar /app/KebunJio.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "KebunJio.jar"]