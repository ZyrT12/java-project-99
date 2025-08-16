# app/Makefile

run-dist:
	./build/install/app/bin/app

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
	./gradlew checkstyleMain

setup:
	./gradlew wrapper --gradle-version 8.13
	./gradlew clean build installDist

build-run:
	make build
	make run

.PHONY: run-dist clean build install run report lint setup build-run
