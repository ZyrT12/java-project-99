import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	application
	jacoco
	checkstyle
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "6.2.0.5505"
	id("io.sentry.jvm.gradle") version "4.4.1"
	id("io.freefair.lombok") version "8.4"
}

repositories {
	mavenCentral()
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-web")      // даёт Jackson и прочее
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")

	implementation("io.javalin:javalin-bundle:6.1.3")

	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
	implementation("org.mindrot:jbcrypt:0.4")
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("at.favre.lib:bcrypt:0.10.2")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("io.rest-assured:rest-assured:5.4.0")
	testImplementation("org.assertj:assertj-core:3.26.0")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.h2database:h2")

	testImplementation(kotlin("test"))
}

configurations.all {
	exclude(group = "org.eclipse.jetty")
}

jacoco {
	toolVersion = "0.8.11"
}

application {
	mainClass.set("hexlet.code.AppApplication")
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

sonar {
	properties {
		property("sonar.projectKey", "ZyrT12_java-project-99")
		property("sonar.organization", "zyrt12")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}

tasks.sonar {
	dependsOn(tasks.jacocoTestReport)
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
		csv.required.set(false)
	}
	classDirectories.setFrom(files("build/classes/java/main"))
	sourceDirectories.setFrom(files("src/main/java"))
	executionData.setFrom(fileTree(buildDir).include("jacoco/test.exec"))
}
