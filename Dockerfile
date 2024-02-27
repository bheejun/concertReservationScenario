# Use the OpenJDK image to build your application
FROM openjdk:17-jdk-slim AS build

# Set the working directory inside the container
WORKDIR /workspace/app

# Copy the Gradle configuration files
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

# Build the application
RUN ./gradlew build

# Move to the application's jar file
WORKDIR /workspace/app/build/libs

# We can optionally use a smaller JDK for running the application
FROM openjdk:17-jdk-slim

# Expose the port the app runs on
EXPOSE 8080

# Copy the jar from the previous stage
COPY --from=build /workspace/app/build/libs/*.jar /app/concert-0.0.1-SNAPSHOT.jar

# Command to run the application
CMD ["java", "-jar", "/app/concert-0.0.1-SNAPSHOT.jar"]
