FROM openjdk:17-jdk-slim
WORKDIR /app

# JAR file কপি করুন
COPY target/Jugjiggasha-1.0.0-SNAPSHOT.jar app.jar

# Port expose করুন
EXPOSE 8080

# JAR ফাইল চালানোর জন্য কমান্ড
ENTRYPOINT ["java", "-jar", "app.jar"]