# ───────────────────────────────────────
#  BUILD STAGE
# ───────────────────────────────────────
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

# Cache dependencies
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B --no-transfer-progress

# Build the application
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests --no-transfer-progress

# ───────────────────────────────────────
#  RUNTIME STAGE
# ───────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

# Non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy your specific jar name
COPY --from=builder /build/target/motorbikebackend-0.0.1-SNAPSHOT.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

# Production-ready JVM flags
ENV JAVA_OPTS="\
    -XX:InitialRAMPercentage=75.0 \
    -XX:MaxRAMPercentage=80.0 \
    -XX:+UseParallelGC \
    -XX:+UseStringDeduplication \
    -XX:MaxMetaspaceSize=256m \
    -Djava.security.egd=file:/dev/./urandom"

# Spring Boot will use $PORT from Render
ENV PORT=10000
EXPOSE 10000

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar