run-dist: install
	./app/build/install/app/bin/app

clean:
	cd app && ./gradlew clean

build:
	cd app && ./gradlew clean build

install:
	cd app && ./gradlew clean installDist

run:
	cd app && ./gradlew run

report:
	cd app && ./gradlew jacocoTestReport

lint:
	cd app && ./gradlew checkstyleMain

setup:
	cd app && chmod +x gradlew && ./gradlew wrapper --gradle-version 8.13 && ./gradlew clean build installDist

build-run:
	$(MAKE) build
	$(MAKE) run

.PHONY: run-dist clean build install run report lint setup build-run
