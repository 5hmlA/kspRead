
plugins {
    //https://github.com/google/ksp/releases
    //libs.plugins.android.application必须在最上面
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
//    alias(libs.plugins.kotlin.android)
}

ksp {
    arg("option1", "value1")
    arg("option2", "value2")
}
android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        setProperty("archivesBaseName", "custom_apk_name")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    namespace = "com.example.ksptt"
}

dependencies {
    implementation(project(mapOf("path" to ":ksp-processor")))
    ksp(project(mapOf("path" to ":ksp-processor")))
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.android.accompanist)
    implementation(libs.bundles.android.project)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.bundles.android.view)
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.bundles.androidx.benchmark)
}