# --------- Stage 1: Build the JAR ----------
FROM openjdk:17-jdk-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y maven
WORKDIR /app

# Copy your Maven project files
COPY pom.xml .
COPY src ./src

# Package the application (without running tests, optional)
RUN mvn clean package -DskipTests

# --------- Stage 2: Run the JAR ----------
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
