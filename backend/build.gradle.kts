import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.21"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "de.kingsware.md2thermal"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Markdown Parser + HTML Renderer
    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")

    // OpenHTMLtoPDF für HTML-Rendering in PDF
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.1.22")

    // Apache PDFBox für direkte Anpassung
    implementation("org.apache.pdfbox:pdfbox:2.0.27")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation(kotlin("reflect"))
}
