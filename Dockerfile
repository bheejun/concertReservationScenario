# Build Stage
FROM gradle:7.3.3-jdk17 as builder

# Copy source code to the container image
WORKDIR /concert
COPY . .
RUN gradle build --no-daemon
FROM openjdk:17-jdk-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /concert/build/libs/concert-0.0.1-SNAPSHOT.jar /concert/concert-0.0.1-SNAPSHOT.jar

# Run the web service on container startup.
CMD ["java", "-jar", "/app/app.jar"]

