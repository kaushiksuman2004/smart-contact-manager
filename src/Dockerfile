# FROM maven:3.8.5-openjdk-17 AS build
# COPY . .
# RUN mvn clean package -DskipTests

# FROM openjdk:17.0.1-jdk-slim
# COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
# EXPOSE 8125
# ENTRYPOINT ["java","-jar","demo.jar"]

# Use an official OpenJDK runtime as the base image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the jar file to the container
COPY target/spring-boot-app.jar /app/

# Expose the port that the application listens on
EXPOSE 8080

# Set the entrypoint command
ENTRYPOINT ["java", "-jar", "spring-boot-app.jar"]