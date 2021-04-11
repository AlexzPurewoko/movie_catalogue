import modules.Apps
import modules.Libs
import modules.TestLibs

plugins {
    id ("com.android.library")
    kotlin ("android")
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
    api(project(":library:data"))
    implementation(kotlin("reflect"))
//    implementation ("androidx.appcompat:appcompat:1.2.0")
//    implementation ("com.google.android.material:material:1.3.0")
//
//    listOf(
//        Libs.paging,
//        Libs.room,
//        Libs.roomKtx,
//        Libs.koinCore,
//        Libs.koinAndroid
//    ).forEach { implementation(it) }

    testImplementation(TestLibs.junit)

    testImplementation(TestLibs.archCoreTesting)
    testImplementation(Libs.coroutinesCore)
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.3")
    testImplementation(TestLibs.paging)
    testImplementation("org.robolectric:robolectric:4.5.1")
    testImplementation("androidx.test:core:1.3.0")

    testImplementation("io.mockk:mockk:1.10.6")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
