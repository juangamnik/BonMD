
plugins {
    // Kotlin JVM plugin with version
    kotlin("jvm") version "2.1.10"
    // Spring Boot plugin for building Spring Boot applications
    id("org.springframework.boot") version "3.4.3"
    // Spring dependency management plugin for managing dependencies
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("plugin.spring") version "2.1.10"
}

group = "de.kingsware.md2thermal"
version = "1.0.0"

repositories {
    // Use Maven Central for resolving dependencies
    mavenCentral()
}

dependencies {
    // Spring Boot Starter for building web applications
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Flexmark for Markdown parsing:
    // Alternatively, use the all-in-one dependency which includes all extensions:
    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")

    // OpenHTMLtoPDF for rendering HTML as PDF
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.1.22")

    // Apache PDFBox for additional PDF manipulation
    implementation("org.apache.pdfbox:pdfbox:2.0.27")

    // Spring Boot Starter Test for testing support
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Kotlin Reflection library
    implementation(kotlin("reflect"))
}