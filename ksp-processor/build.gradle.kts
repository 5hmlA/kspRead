import com.google.wireless.android.sdk.stats.GradleBuildVariant.KotlinOptions

plugins {
    alias(libs.plugins.kotlin.jvm)
}
kotlin {
    // Or shorter:
    jvmToolchain(18)
}

dependencies {
    implementation("com.squareup:kotlinpoet-ksp:1.16.0")
    implementation(libs.ksp.process.api)
}