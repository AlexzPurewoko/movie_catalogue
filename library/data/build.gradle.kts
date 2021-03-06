import modules.Apps
import modules.KaptLibs
import modules.Libs
import java.io.FileInputStream
import java.util.*

val apiToken: String by project
val baseUrl: String by project

plugins {
    id("com.android.library")
    kotlin("android")
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

        loadConfigField(this)
//        print(project.properties)
//        buildConfigField()
//        buildConfigField("String", "API_TOKEN", apiToken)
//        buildConfigField("String", "BASE_URL", baseUrl)
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs =
            freeCompilerArgs.toMutableList().apply { add("-Xopt-in=kotlin.RequiresOptIn") }
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
        Libs.sqliteChiper,

        Libs.koinCore,
        Libs.koinAndroid
    ).forEach { api(it) }

    kapt(KaptLibs.roomCompiler)

    testImplementation(project(":test:libs"))
    androidTestImplementation(project(":test:androidtestlibs"))
    androidTestImplementation(project(":test:libs")) {
        isTransitive = false
    }
}

fun loadConfigField(scope: com.android.build.api.dsl.VariantDimension) {
    val properties = Properties()
    properties.load(FileInputStream(rootProject.file("config.properties")))

    listOf(
        "API_TOKEN", "BASE_URL", "DB_PASSPHRASE"
    ).forEach {
        scope.buildConfigField("String", it, properties.getProperty(it))
    }
}