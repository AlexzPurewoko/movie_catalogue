import modules.Apps
import modules.Libs

plugins {
    id ("com.android.library")
    kotlin ("android")
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
        consumerProguardFiles( "consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            debuggable(true)
        }
    }

    buildFeatures {
        viewBinding = true
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

    api(project(":library:core"))
    listOf(
        Libs.kotlin,
        Libs.coreKtx,
        Libs.appcompat,
        Libs.kotlinStd,
        Libs.constraintLayout,
        Libs.cardView,
            Libs.fragmentKtx
//        Libs.koinCore

    ).forEach { api(it) }
    listOf(
//            Libs.kotlinStd,
//            Libs.coreKtx,
//            Libs.constraintLayout,
            Libs.circularProgressBar,
            Libs.glide,
//            Libs.paging,
            Libs.coroutinesCore,
            Libs.coroutinesAndroid,
            Libs.corbindCore,
            Libs.corbindMaterial,
            Libs.corbindAppCompat,

//            Libs.koinCore,
//            Libs.koinAndroid,
            Libs.koinAndroidViewModel,
            Libs.lottie,
            Libs.shimmer
    ).forEach { api(it) }
    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation ("androidx.annotation:annotation:1.1.0")
}
