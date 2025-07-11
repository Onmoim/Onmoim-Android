import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

group = "com.onmoim.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "onmoim.android.hilt"
            implementationClass = "com.onmoim.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "onmoim.kotlin.hilt"
            implementationClass = "com.onmoim.HiltKotlinPlugin"
        }

        // Kotlin
        register("kotlinLibrary") {
            id = "onmoim.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("kotlinLibrarySerialization") {
            id = "onmoim.kotlin.library.serialization"
            implementationClass = "KotlinLibrarySerializationConventionPlugin"
        }
    }
}