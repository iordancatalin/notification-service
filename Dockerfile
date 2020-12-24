FROM maven:3.6.3-jdk-11 as buildStage

COPY . /app
WORKDIR /app

RUN mvn clean package

FROM openjdk:11-jre-slim

ENV SPRING_PROFILES_ACTIVE=docker
COPY --from=buildStage /app/target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service.jar

WORKDIR /app
EXPOSE 8081

ENTRYPOINT [ "java", "-jar", "notification-service.jar" ]