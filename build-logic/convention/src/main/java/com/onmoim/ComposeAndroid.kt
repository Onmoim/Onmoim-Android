package com.onmoim

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureComposeAndroid() {
    with(plugins) {
        apply("org.jetbrains.kotlin.plugin.compose")
    }

    androidExtension.apply {
        dependencies {
            val bom = findLibrary("androidx-compose-bom")
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))

            add("implementation", findLibrary("androidx.ui"))
            add("implementation", findLibrary("androidx.ui.graphics"))
            add("implementation", findLibrary("androidx.ui.tooling.preview"))
            add("implementation", findLibrary("androidx.material3"))
            add("androidTestImplementation", findLibrary("androidx.espresso.core"))
            add("androidTestImplementation", findLibrary("androidx.ui.test.junit4"))
            add("debugImplementation", findLibrary("androidx.ui.tooling"))
            add("debugImplementation", findLibrary("androidx.ui.test.manifest"))
        }
    }

    extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
        includeSourceInformation.set(true)
    }
}

/**
 * Compose Library
 */
fun Project.configureComposeFeature() {
    androidExtension.apply {
        with(plugins) {
            apply("org.jetbrains.kotlin.plugin.compose")
        }
    }
}