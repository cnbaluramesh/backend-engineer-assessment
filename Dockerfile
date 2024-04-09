# Build stage
FROM openjdk:21-jdk-slim as build
LABEL authors="C N Balu Ramesh"
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Run stage
FROM openjdk:21-jdk-slim
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/libs
COPY --from=build ${DEPENDENCY}/*.jar app.jar