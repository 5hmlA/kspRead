plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":ksp-processor"))
    ksp(project(":ksp-processor"))
}