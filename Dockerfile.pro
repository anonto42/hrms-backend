# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build

ARG BUILD_VERSION=1.0.0
ARG BUILD_PROFILE=prod

WORKDIR /app

# Copy Maven files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execution permission to mvnw
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN ./mvnw clean package -DskipTests -P${BUILD_PROFILE} -Dbuild.version=${BUILD_VERSION}

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

ARG BUILD_VERSION=1.0.0
ARG JAR_FILE=*.jar

LABEL maintainer="your-team@example.com"
LABEL version=${BUILD_VERSION}
LABEL description="HRMS Backend Application"

# Install curl for health checks
RUN apk add --no-cache curl

WORKDIR /app

# Create directory for uploads
RUN mkdir -p /app/uploads && chmod 755 /app/uploads

# Copy jar file
COPY --from=build /app/target/${JAR_FILE} app.jar

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application with production settings
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-Dserver.port=8080", \
    "-jar", \
    "app.jar"]