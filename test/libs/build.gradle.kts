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
    implementation(Libs.kotlinStd)
    listOf(
        TestLibs.junit,
        TestLibs.archCoreTesting,
        Libs.coroutinesCore,
        TestLibs.corountineXTest,
        TestLibs.paging,
        TestLibs.androidXCore,
        TestLibs.corountineXTest,
        TestLibs.robolectric,
        TestLibs.mockk
    ).forEach { api(it) }

    api(project(":library:core"))
}
