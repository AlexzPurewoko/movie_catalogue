import modules.AndroidTestLibs
import modules.Apps
import modules.Libs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    id("jacoco-plugin")
}

apply(plugin = "androidx.navigation.safeargs.kotlin")

android {
    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion(Apps.buildToolsVersion)

    defaultConfig {
        applicationId = Apps.applicationId
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    dynamicFeatures =
        mutableSetOf(":feature:search", ":feature:discover", ":feature:favorite", ":feature:detail")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    listOf(":library:core", ":library:data", ":library:res")
        .forEach { implementation(project(it)) }

    listOf(
        Libs.navigationUiKtx,
        Libs.navigationKtx,
        Libs.navigationDynamicModule
    ).forEach { api(it) }

    listOf(
        Libs.leakCanary,
        AndroidTestLibs.fragment,
        AndroidTestLibs.espressoIdlingResource,
        project(":test:assetDebug")
    ).forEach {
        debugImplementation(it)
    }
}
