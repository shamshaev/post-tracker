clean:
	./gradlew clean

build:
	./gradlew clean build

test:
	./gradlew test

lint:
	./gradlew checkstyleMain checkstyleTest

report:
	./gradlew jacocoTestReport

start:
	./gradlew bootRun

.PHONY: build
