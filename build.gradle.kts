plugins {
	java
	war
	checkstyle
	jacoco
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("io.freefair.lombok") version "8.6"
}

group = "shamshaev.code"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring
	implementation("org.springframework.boot:spring-boot-starter-web")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	// ORM
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	// Database
	implementation("org.postgresql:postgresql:42.7.3")
	implementation("jakarta.validation:jakarta.validation-api:3.1.0")
	// Utils
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	implementation("org.apache.commons:commons-csv:1.12.0")
	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
	testImplementation("org.testcontainers:junit-jupiter:1.18.3")
	testImplementation("org.testcontainers:testcontainers:1.18.3")
	testImplementation("org.testcontainers:postgresql:1.18.3")
	implementation("org.instancio:instancio-junit:3.6.0")
	implementation("net.datafaker:datafaker:2.0.2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport { reports { xml.required.set(true) } }
