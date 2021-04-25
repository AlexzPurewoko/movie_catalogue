import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.fileTree
import org.gradle.kotlin.dsl.jacoco
import org.gradle.kotlin.dsl.task
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    jacoco
}

jacoco {
    toolVersion = modules.Versions.jacoco
}

task("jacocoTestReport", type = JacocoReport::class){
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/data/models/*"
    )
    val mainSrc = "${project.projectDir}/src/main/java"
    val javaTree = fileTree(
        "dir" to "${project.buildDir}/intermediates/javac/debug/classes",
        "excludes" to fileFilter
    )

    val kotlinTree = fileTree(
        "dir" to "${project.buildDir}/tmp/kotlin-classes/debug",
        "excludes" to fileFilter
    )

    sourceDirectories.setFrom(files(listOf(mainSrc)))
    classDirectories.from(files(listOf(javaTree, kotlinTree)))
    executionData.from(fileTree(
        "dir" to "$buildDir",
        "includes" to listOf(
            "jacoco/testDebugUnitTest.exec",
            "outputs/code-coverage/connected/*coverage.ec"
        )
    ))
}

tasks.withType<Test> {
    if(name == "testDebugUnitTest" || name == "testReleaseUnitTest")
        finalizedBy("jacocoTestReport")
}