# --------- Stage 1: Build the application ----------
FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files first for better layer caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# --------- Stage 2: Run the application ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]