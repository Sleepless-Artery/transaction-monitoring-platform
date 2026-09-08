plugins {
	kotlin("jvm") version "2.3.21"
	kotlin("plugin.spring") version "2.3.21"

	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"

	id("org.jooq.jooq-codegen-gradle") version "3.21.7"
}

group = "org.sleepless_artery"
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
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Database
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-liquibase")

	runtimeOnly("org.postgresql:postgresql")
	jooqCodegen("org.postgresql:postgresql")

	// Kafka
	implementation("org.springframework.boot:spring-boot-starter-kafka")

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")

	// Tests
	testImplementation("org.springframework.boot:spring-boot-starter-jooq-test")
	testImplementation("org.springframework.boot:spring-boot-starter-kafka-test")
	testImplementation("org.springframework.boot:spring-boot-starter-liquibase-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")

	testImplementation("org.springframework.boot:spring-boot-testcontainers")

	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
	testImplementation("org.mockito:mockito-junit-jupiter")

	testImplementation("org.testcontainers:testcontainers-junit-jupiter")
	testImplementation("org.testcontainers:testcontainers-kafka")
	testImplementation("org.testcontainers:testcontainers-postgresql")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	sourceSets {
		main {
			kotlin.srcDir("build/generated-src/jooq/main")
		}
	}

	compilerOptions {
		freeCompilerArgs.addAll(
			"-Xjsr305=strict",
			"-Xannotation-default-target=param-property"
		)
	}
}

jooq {
	configuration {
		jdbc {
			driver = "org.postgresql.Driver"

			url = System.getenv("JOOQ_DB_URL")
				?: "jdbc:postgresql://localhost:5432/transaction_db"

			user = System.getenv("JOOQ_DB_USERNAME")
				?: "transaction"

			password = System.getenv("JOOQ_DB_PASSWORD")
				?: "transaction"
		}

		generator {
			name = "org.jooq.codegen.KotlinGenerator"

			database {
				name = "org.jooq.meta.postgres.PostgresDatabase"
				inputSchema = "public"
				includes = "transactions"
			}

			generate {
				isPojos = false
				isDaos = false
				isRecords = true

				isKotlinNotNullRecordAttributes = true
			}

			target {
				packageName =
					"org.sleepless_artery.transaction_api.infrastructure.persistence.jooq.generated"

				directory = "build/generated-src/jooq/main"
			}
		}
	}
}

tasks.named("compileKotlin") {
	dependsOn("jooqCodegen")
}

tasks.withType<Test> {
	useJUnitPlatform()
}