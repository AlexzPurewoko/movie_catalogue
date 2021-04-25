import modules.AndroidTestLibs
import modules.Apps
import modules.Libs
import modules.TestLibs
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

        testInstrumentationRunner = "id.apwdevs.app.movieshow.runner.AppTestRunner"
        testApplicationId = applicationId
    }

    buildFeatures.viewBinding = true

    sourceSets {
//        ["${project('match the module name that is currently in the dependencies').projectDir}/src/androidTest/java"]
//        getByName("androidTest").apply {
//            val src = java.srcDirs.toMutableSet()
//            src.add(File(project(":app").projectDir, "src/androidTest/java"))
//            java.setSrcDirs(src)
//        }
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

    api(project(":library:res"))
    implementation(project(":app"))
    testImplementation(project(":test:libs"))
    androidTestImplementation(project(":test:libs")) {
        isTransitive = false
    }
    androidTestImplementation(project(":test:androidtestlibs"))

    testImplementation(TestLibs.junit)

    listOf(
        AndroidTestLibs.navigation,
        AndroidTestLibs.androidxJunit,
        AndroidTestLibs.androidxAnnotatation,
        TestLibs.mockWebServer

    ).forEach { androidTestImplementation(it) }



//    debugImplementation(AndroidTestLibs.fragment)
//    androidTestImplementation(Libs.coreKtx)
    androidTestImplementation(project(":app"))
    debugImplementation(project(":app"))
//    androidTestImplementation(TestLibs.junit)
////    androidTestImplementation(project(":res"))
//    androidTestImplementation("androidx.test.ext:junit:1.1.2")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
//    androidTestImplementation("androidx.test:rules:1.3.0")
//    androidTestImplementation ("androidx.annotation:annotation:1.1.0")
}
