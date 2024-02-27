# Use your base image
FROM openjdk:17-jdk-slim AS build

# Install dependencies required for wget and SSL to download dockerize
RUN apt-get update && \
    apt-get install -y wget libssl-dev && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install dockerize
ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

WORKDIR /app

COPY ./build/libs/concert-0.0.1-SNAPSHOT.jar /app/concert-0.0.1-SNAPSHOT.jar


EXPOSE 8080

CMD ["java", "-jar", "/app/concert-0.0.1-SNAPSHOT.jar"]