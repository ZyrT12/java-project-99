run-dist:
	./build/install/java-project-99/bin/java-project-99

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew clean installDist

run:
	./gradlew run

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain checkstyleTest

setup:
	./gradlew wrapper --gradle-version 8.13
	./gradlew clean build installDist

sonar:
	./gradlew sonar

build-run:
	make build
	make run

.PHONY: run-dist clean build install run report lint setup sonar build-run
