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

jacoco {
	toolVersion = "0.8.11"
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

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.sonar {
	dependsOn(tasks.jacocoTestReport)
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport) // после тестов
	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
		events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
		showStandardStreams = true
	}
	reports {
		junitXml.required.set(true) // убедимся, что XML точно есть
		html.required.set(true)
	}

	systemProperty("junit.jupiter.execution.parallel.enabled", "false")
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