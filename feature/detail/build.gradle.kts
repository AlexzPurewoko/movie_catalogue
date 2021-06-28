import modules.Apps
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.dynamic-feature")
    kotlin("android")
    id("kotlin-parcelize")
    id("jacoco-plugin")
}

android {
    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion(Apps.buildToolsVersion)

    defaultConfig {
        applicationId = "id.apwdevs.app.detail"
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName

        testInstrumentationRunner = "id.apwdevs.app.test.androdtest.runner.AppTestRunner"
        testApplicationId = applicationId
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures.viewBinding = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    api(project(":library:res"))
    implementation(project(":app"))
    testImplementation(project(":test:libs"))
    androidTestImplementation(project(":test:libs")) {
        isTransitive = false
    }
    androidTestImplementation(project(":test:androidtestlibs"))
}
