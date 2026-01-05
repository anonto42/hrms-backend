plugins {
	java
	id("org.springframework.boot") version "3.5.9"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.template"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {

	// JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// Mongodb
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

	// Spring Security
	implementation("org.springframework.boot:spring-boot-starter-security")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Email & Mail Support
	implementation("org.springframework.boot:spring-boot-starter-mail")
//	implementation("com.sun.mail:jakarta.mail:2.1.3")

	// Thymeleaf for Email Templates
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
	implementation("org.thymeleaf:thymeleaf-spring5:3.1.2.RELEASE")

	// DotEnv
	implementation("io.github.cdimascio:dotenv-java:3.0.0")

	// Input Validator
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Web MVC
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
    runtimeOnly("com.mysql:mysql-connector-j")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
