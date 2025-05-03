# Use Maven with OpenJDK 17 for the build
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy the project files
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the project
COPY src ./src
RUN mvn clean package -DskipTests

# Use OpenJDK 17 to run the built JAR
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/x-matrix-backend.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
