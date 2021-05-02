import modules.Apps
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.dynamic-feature")
    kotlin("android")
    id("jacoco-plugin")
}

android {
    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion(Apps.buildToolsVersion)

    defaultConfig {
        applicationId = "id.apwdevs.app.discover"
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName

        testInstrumentationRunner = "id.apwdevs.app.test.androdtest.runner.AppTestRunner"
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

//    listOf(
//        Libs.kotlin,
//        Libs.coreKtx,
//
//                Libs.koinCore,
//            Libs.koinAndroid,
//            Libs.koinAndroidViewModel
//    ).forEach { implementation(it) }
//    testIMplementation(":t")
//    testImplementation("junit:junit:4.+")
//    androidTestImplementation("androidx.test.ext:junit:1.1.2")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
//    androidTestImplementation("androidx.annotation:annotation:1.1.0")
}
