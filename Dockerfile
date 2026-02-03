# Use official OpenJDK 21 image (slim variant)
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built jar from local target folder
COPY target/motorbikebackend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Render assigns
EXPOSE 8080
ENV PORT=8080

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
