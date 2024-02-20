import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.graalvm.buildtools.native") version "0.9.28"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "com.santannaf.rinha-backend-2024-webflux"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    runtimeOnly("org.postgresql:r2dbc-postgresql")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

graalvmNative {
    binaries {
        named("main") {
            imageName.set("app")
            configurationFileDirectories.from(file("src/main/resources/META-INF/native-image"))
            mainClass.set("org.example.rinhabackend2024kotlin.ApplicationKt")
            buildArgs.add("--color=always")
            buildArgs.add("--report-unsupported-elements-at-runtime")
            buildArgs.add("--allow-incomplete-classpath")
            buildArgs.add("--enable-preview")
            buildArgs.add("--verbose")
            buildArgs.add("--initialize-at-build-time=ch.qos.logback.classic.Logger,ch.qos.logback.core.status.InfoStatus,ch.qos.logback.core.util.Loader,ch.qos.logback.core.util.StatusPrinter,org.slf4j.LoggerFactory,ch.qos.logback.core.status.StatusBase,ch.qos.logback.classic.Level")
        }
    }
}
