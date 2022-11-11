import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("java")
    application
}

group = "com.caldremch.asm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val asm_version = "9.2"
dependencies {
    testImplementation(kotlin("test"))
    api ("org.ow2.asm:asm:$asm_version")
    api ("org.ow2.asm:asm-analysis:$asm_version")
    api ("org.ow2.asm:asm-commons:$asm_version")
    api ("org.ow2.asm:asm-tree:$asm_version")
    api ("org.ow2.asm:asm-util:$asm_version")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}