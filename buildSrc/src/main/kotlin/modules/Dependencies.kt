package modules

import sun.misc.Version

@Suppress("unused")
object Libs {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"
    const val savedStateModule = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycleVersion}"

    // room
    const val room = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"

    // cardview
    const val cardView  = "androidx.cardview:cardview:${Versions.cardViewVersion}"

    // workmanager
    const val workManagerRuntime = "androidx.work:work-runtime:${Versions.workManager}"
    const val workManagerKtx = "androidx.work:work-runtime-ktx:${Versions.workManager}"

    //fragment
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"

    // navigation
    const val navigation = "androidx.navigation:navigation-fragment:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui:${Versions.navigation}"
    const val navigationKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigationDynamicModule = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}"

    //paging
    const val paging = "androidx.paging:paging-runtime:${Versions.paging}"

    // retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    // logging interceptor
    const val loggingInterceptor =  "com.squareup.okhttp3:logging-interceptor:${Versions.loggingInterceptor}"

    // glide
    const val glide ="com.github.bumptech.glide:glide:${Versions.glide}"

    // coroutines
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val corbindCore = "ru.ldralighieri.corbind:corbind:${Versions.corbind}"
    const val corbindAppCompat = "ru.ldralighieri.corbind:corbind-appcompat:${Versions.corbind}"
    const val corbindMaterial = "ru.ldralighieri.corbind:corbind-material:${Versions.corbind}"

    // koin

    const val koinAndroid = "org.koin:koin-android:${Versions.koin}"
    const val koinCore = "org.koin:koin-core:${Versions.koin}"
    const val koinAndroidViewModel = "org.koin:koin-android-viewmodel:${Versions.koin}"

    //hilt
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"

    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val googleMaterial = "com.google.android.material:material:${Versions.material}"

    //others
    const val circularProgressBar = "com.mikhaellopez:circularprogressbar:${Versions.customProgressBar}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
    const val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.mockWebServer}"
}

object KaptLibs {
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycleVersion}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
    const val koinAndroid = "org.koin:koin-test:${Versions.koin}"
    const val archCoreTesting = "androidx.arch.core:core-testing: ${Versions.archVersion}"
    const val roomTest = "androidx.room:room-testing:${Versions.roomVersion}"
    const val paging = "androidx.paging:paging-common:${Versions.paging}"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.mockWebServer}"
}

object AndroidTestLibs {
    const val workManagerTest = "androidx.work:work-testing:${Versions.workManager}"
    const val fragment = "androidx.fragment:fragment-testing:${Versions.fragment}"
    const val navigation = "androidx.navigation:navigation-testing:${Versions.navigation}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espressoIdlingResource = "androidx.test.espresso:espresso-idling-resource:${Versions.espresso}"
    const val androidxJunit = "androidx.test.ext:junit:1.1.2"
    const val androidxAnnotatation = "androidx.annotation:annotation:1.1.0"
}
