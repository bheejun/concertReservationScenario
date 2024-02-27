FROM openjdk:17-jdk-slim AS build

WORKDIR /workspace/app

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
EXPOSE 8080

COPY --from=build /workspace/app/build/libs/*.jar /app/app.jar

COPY entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

CMD ["java", "-jar", "/app/app.jar"]
