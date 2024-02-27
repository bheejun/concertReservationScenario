# Use the OpenJDK image to build your application
FROM openjdk:17-jdk-slim AS build

# Set the working directory inside the container
WORKDIR /workspace/app

# Copy the Gradle configuration files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew build -x test

# Move to the application's jar file
WORKDIR /workspace/app/build/libs

FROM openjdk:17-jdk-slim


RUN apt-get update && apt-get install -y curl gnupg
RUN echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] http://packages.cloud.google.com/apt cloud-sdk main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
RUN curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
RUN apt-get update && apt-get install -y google-cloud-sdk

EXPOSE 8080

RUN mkdir -p /app/config

COPY --from=build /workspace/app/build/libs/*.jar /app/app.jar

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

CMD ["/entrypoint.sh"]

