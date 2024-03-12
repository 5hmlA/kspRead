import com.google.wireless.android.sdk.stats.GradleBuildVariant.KotlinOptions

plugins {
//    kotlin("jvm") version (libs.versions.kotlin.asProvider())
    alias(libs.plugins.kotlin.jvm)
}
kotlin {
    // Or shorter:
    jvmToolchain(17)

//    target {
//        compilerOptions{
//        }
//    }
}

dependencies {
    implementation("com.squareup:kotlinpoet-ksp:1.16.0")
    implementation(libs.ksp.process.api)
}