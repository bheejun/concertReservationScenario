FROM openjdk:17-jdk-slim
LABEL maintainer=jun
WORKDIR /app
COPY build/docker/libs libs/
COPY build/docker/resources resources/
COPY build/docker/classes classes/
ENTRYPOINT ["java", "-Dspring.profiles.active=production", "-Xmx2048m", "-cp", "/app/resources:/app/classes:/app/libs/*", "com.example.concert.ConcertApplicationKt"]
EXPOSE 8080 8080