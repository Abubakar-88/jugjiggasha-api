# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

# Install necessary packages
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create app user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# Set working directory
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/jugjiggasha-backend-1.0.0.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost/Jugjiggasha/api/questions || exit 1

# Expose port
EXPOSE 8080

# JVM options for production
ENV JAVA_OPTS="-Xmx512m -Xms256m -Dspring.profiles.active=prod"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]