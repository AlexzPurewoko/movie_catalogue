import modules.AndroidTestLibs
import modules.Apps
import modules.Libs
import modules.TestLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id ("com.android.application")
    kotlin ("android")
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug"){
            debuggable(true)
            applicationIdSuffix = ".debug"
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
        Libs.kotlin,
        Libs.coreKtx,
        Libs.appcompat,
        Libs.googleMaterial,
        Libs.constraintLayout,
        Libs.koinCore,
        Libs.koinAndroid
    ).forEach { implementation(it) }

    listOf(
        Libs.navigationUiKtx,
            Libs.navigationKtx,
            Libs.navigationDynamicModule
    ).forEach{ api(it) }
    testImplementation ("junit:junit:4.+")
    debugImplementation(AndroidTestLibs.fragment)
    debugImplementation(AndroidTestLibs.espressoIdlingResource)
    debugImplementation(project(":test:assetDebug"))
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")

    listOf(
        AndroidTestLibs.navigation,
        AndroidTestLibs.androidxJunit,
        AndroidTestLibs.androidxAnnotatation,
        TestLibs.mockWebServer

    ).forEach { androidTestImplementation(it) }


    debugImplementation(AndroidTestLibs.fragment)
    androidTestImplementation(Libs.coreKtx)
    androidTestImplementation(TestLibs.junit)
//    androidTestImplementation(project(":res"))
    androidTestImplementation("androidx.test:rules:1.3.0")
}
