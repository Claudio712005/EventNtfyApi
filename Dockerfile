FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/event_ntfy-1.0.0.jar event_ntfy-1.0.0.jar
EXPOSE 8080
CMD ["java", "-jar", "event_ntfy-1.0.0.jar"]