FROM openjdk:17-jdk-slim AS build
WORKDIR /workspace/app
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build --no-daemon -x test

# 실행 환경
FROM openjdk:17-jdk-slim
EXPOSE 8080
WORKDIR /app
RUN mkdir -p /app/config

COPY --from=build /workspace/app/build/libs/*.jar /app/

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
RUN ls -la /entrypoint.sh
CMD ["/entrypoint.sh"]


#FROM openjdk:17-jdk-slim
#
#
#RUN apt-get update && apt-get install -y curl gnupg
#RUN echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] http://packages.cloud.google.com/apt cloud-sdk main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
#RUN curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | gpg --dearmor -o /usr/share/keyrings/cloud.google.gpg
#
#RUN apt-get update && apt-get install -y google-cloud-sdk
#
#EXPOSE 8080
#
#RUN mkdir -p /config
#
#COPY --from=build /workspace/app/build/libs/*.jar /app/
#
#COPY entrypoint.sh /entrypoint.sh
#RUN chmod +xwr /entrypoint.sh
#

