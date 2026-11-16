plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.mateo.cxf-codegen") version "2.4.0"
	id("idea")
}

group = "com.impuls.billing"
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
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	// Security
	implementation("org.springframework.security:spring-security-crypto")

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	// SOAP CXF / JAX-WS Client
	implementation("jakarta.xml.ws:jakarta.xml.ws-api:4.0.2")
	implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
	implementation("org.apache.cxf:cxf-rt-frontend-jaxws:4.0.5")
	implementation("org.apache.cxf:cxf-rt-transports-http:4.0.5")

	compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

	// OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

	// Тесты
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val lanbillingWsdl2Java = tasks.register<io.mateo.cxf.codegen.wsdl2java.Wsdl2Java>("lanbillingWsdl2Java") {
	toolOptions {
		wsdl.set(file("docs/wsdl.xml").absolutePath)
		packageNames.set(listOf("com.impuls.billing.soap.generated"))
		markGenerated.set(true)
		autoNameResolution.set(true)
	}
}

sourceSets {
	main {
		java {
			srcDir(lanbillingWsdl2Java)
		}
	}
}

tasks.named("compileKotlin") {
	dependsOn(lanbillingWsdl2Java)
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	enabled = true
}

idea {
	module {
		generatedSourceDirs.add(file("build/lanbillingWsdl2Java-wsdl2java-generated-sources"))
	}
}