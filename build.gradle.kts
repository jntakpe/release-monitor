import org.gradle.kotlin.dsl.*

group = "com.github.jntakpe"
version = "0.1.0-SNAPSHOT"

buildscript {
    repositories {
        jcenter()
        maven("https://repo.spring.io/snapshot")
        maven("https://repo.spring.io/milestone")
    }
    dependencies {
        val springBootVersion = "2.0.0.M4"
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    }
}

plugins {
    val kotlinVersion = "1.1.51"
    val springIOVersion = "1.0.3.RELEASE"
    kotlin("jvm")
    kotlin("plugin.noarg") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    id("io.spring.dependency-management") version springIOVersion
}

apply {
    plugin("org.springframework.boot")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val assertJVersion = "3.8.0"
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
    compile("org.springframework.boot:spring-boot-starter-actuator")
    compile("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    compile("org.springframework.boot:spring-boot-starter-webflux")
    testCompile("org.springframework.boot:spring-boot-starter-test")
    testCompile("io.projectreactor:reactor-test")
    testCompile("org.springframework.restdocs:spring-restdocs-mockmvc")
    testCompile("org.assertj:assertj-core:$assertJVersion")
}

repositories {
    jcenter()
    maven("https://repo.spring.io/snapshot")
    maven("https://repo.spring.io/milestone")
}
