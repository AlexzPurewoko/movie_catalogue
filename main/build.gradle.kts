import modules.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id ("com.android.dynamic-feature")
    kotlin("android")
}
android {
    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion(Apps.buildToolsVersion)

    defaultConfig {
        applicationId = "id.apwdevs.app.main"
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

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

    implementation(project(":app"))

    listOf(
        Libs.kotlin,
        Libs.coreKtx
    ).forEach { implementation(it) }

    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation ("androidx.annotation:annotation:1.1.0")
}
