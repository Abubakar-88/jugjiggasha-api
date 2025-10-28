# Multi-stage build ব্যবহার করুন
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Port expose করুন
EXPOSE 8080

# JAR ফাইল চালানোর জন্য কমান্ড
ENTRYPOINT ["java", "-jar", "app.jar"]