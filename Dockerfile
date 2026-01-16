# Build stage
FROM gradle:8.8.0-jdk21 AS builder

WORKDIR /app

# Copy build configuration files
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Download dependencies first (cached layer)
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the application
RUN gradle clean bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN groupadd -g 1001 spring && \
    useradd -r -u 1001 -g spring -s /bin/bash spring
USER spring:spring
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder --chown=spring:spring /app/build/libs/*.jar app.jar

# Add JVM options for better performance in container
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"
ENV SPRING_PROFILES_ACTIVE="docker"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]