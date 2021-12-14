plugins {
    java
    kotlin("jvm") version "1.4.31" apply false
    `kotlin-dsl`
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // vertx
    val vertxVersion = "4.2.1"
    implementation("io.vertx:vertx-core:${vertxVersion}")
    implementation("io.vertx:vertx-codegen:${vertxVersion}")
    implementation("io.vertx:vertx-hazelcast:${vertxVersion}")

    // jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}