// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion = modules.Versions.kotlin
    val navVersion = modules.Versions.navigation
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:4.1.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath ( "androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion" )
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete (rootProject.buildDir)
}
