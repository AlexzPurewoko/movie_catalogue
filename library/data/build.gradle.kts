import modules.*

plugins {
    id ("com.android.library")
    kotlin ("android")
    kotlin("kapt")
    id("jacoco-plugin")
}

android {
    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion(Apps.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName

        testInstrumentationRunner = "id.apwdevs.app.test.androdtest.runner.AppTestRunner"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs.toMutableList().apply { add("-Xopt-in=kotlin.RequiresOptIn") }
    }

}

dependencies {

    listOf(
        Libs.retrofit,
        Libs.retrofitGson,
        Libs.paging,
        Libs.loggingInterceptor,
        Libs.room,
        Libs.roomKtx,

        Libs.koinCore,
        Libs.koinAndroid
    ).forEach { api(it) }

    kapt(KaptLibs.roomCompiler)

//    testImplementation(TestLibs.junit)
//    debugImplementation(project(":test:assetDebug"))
//
//    listOf(
//            AndroidTestLibs.androidxJunit,
//            AndroidTestLibs.espressoCore,
//            AndroidTestLibs.androidxAnnotatation
//    ).forEach { androidTestImplementation(it) }
//
//    testImplementation(project(":test:libs"))
//    androidTestImplementation(project(":test:libs"))
    testImplementation(project(":test:libs"))
    androidTestImplementation(project(":test:androidtestlibs"))
    androidTestImplementation(project(":test:libs")) {
        isTransitive = false
    }
}
configurations.all {
    resolutionStrategy {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
    }
}
