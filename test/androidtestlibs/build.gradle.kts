import modules.AndroidTestLibs
import modules.Apps
import modules.Libs
import modules.TestLibs

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
}


android {
    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion(Apps.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            debuggable(true)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    listOf(
        Libs.kotlinStd,
        project(":app"),
        AndroidTestLibs.espressoCore,
        AndroidTestLibs.androidxJunit,
        AndroidTestLibs.androidxAnnotatation,
        AndroidTestLibs.navigation,
        "androidx.test.espresso:espresso-contrib:3.3.0",
        "androidx.test:rules:1.3.0",
        project(":library:core"),
        Libs.koinCore,
        Libs.room,
        Libs.retrofit,
        TestLibs.mockWebServer
    ).forEach { api(it) }

}

allprojects {
    configurations.all {
        resolutionStrategy.force("org.objenesis:objenesis:2.6")
    }
}
