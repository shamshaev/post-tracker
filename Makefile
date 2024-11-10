clean:
	./gradlew clean

build:
	./gradlew clean build

clear:
	./gradlew clean
	docker compose -f docker/docker-compose.yml down -v

docker-infra-start:
	docker compose -f docker/docker-compose.yml up -d -V --remove-orphans

test:
	./gradlew test

lint:
	./gradlew checkstyleMain checkstyleTest

report:
	./gradlew jacocoTestReport

start:
	./gradlew bootRun

.PHONY: build
