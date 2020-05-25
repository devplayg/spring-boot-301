import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.72"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
}

group = "com.devplayg"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // Spring Boot
//    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    //implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Thymeleaf layout
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")

    // MySQL
    runtimeOnly("mysql:mysql-connector-java")

    // exposed ORM
    implementation("org.jetbrains.exposed", "exposed-core", "0.24.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.24.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.24.1")
    implementation("org.jetbrains.exposed", "exposed-jodatime", "0.24.1")
//    implementation("org.jetbrains.exposed", "spring-transaction", "0.24.1")

    // Hikari
    implementation("com.zaxxer", "HikariCP", "3.4.2")

    // QueryDSL
    api("com.querydsl:querydsl-jpa:4.2.2")
    implementation("com.querydsl:querydsl-apt:4.2.2:jpa") // ":jpa 꼭 붙여줘야 한다!!"
//    implementation("gradle.plugin.com.ewerk.gradle.plugins:querydsl-plugin:1.0.10")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")


    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
