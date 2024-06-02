plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

ksp {
    arg("option1", "value1")
    arg("option2", "value2")
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