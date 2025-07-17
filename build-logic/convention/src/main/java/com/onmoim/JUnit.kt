package com.onmoim

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroidJUnit() {
    dependencies {
        "testImplementation"(findLibrary("junit"))
        "testImplementation"(findLibrary("androidx-junit"))
    }
}