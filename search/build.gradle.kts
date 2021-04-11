import modules.Apps
import modules.Libs
import modules.TestLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id ("com.android.dynamic-feature")
    kotlin("android")
}

android {
    compileSdkVersion(modules.Apps.compileSdk)
    buildToolsVersion(modules.Apps.buildToolsVersion)

    defaultConfig {
        applicationId = "id.apwdevs.app.search"
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

//    implementation(project(":res"))
    api(project(":res"))
    implementation(project(":app"))
//    listOf(
//        Libs.kotlinStd,
//        Libs.coreKtx,
//        Libs.constraintLayout,
//        Libs.circularProgressBar,
//        Libs.glide,
//        Libs.paging,
//        Libs.coroutinesCore,
//        Libs.coroutinesAndroid,
//        Libs.corbindCore,
//        Libs.corbindMaterial,
//        Libs.corbindAppCompat,
//
//        Libs.koinCore,
//        Libs.koinAndroid,
//        Libs.koinAndroidViewModel
//    ).forEach { implementation(it) }
    testImplementation(TestLibs.junit)
//    testImplementation(project(":core").dependencyProject.sourceSets.getByName("test").output)

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
    androidTestImplementation("androidx.annotation:annotation:1.1.0")
}
