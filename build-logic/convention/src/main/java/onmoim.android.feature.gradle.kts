import com.onmoim.configureHiltAndroid
import com.onmoim.findLibrary

plugins {
    id("onmoim.android.library")
    id("onmoim.android.compose")
}

android {
    packaging {
        resources {
            excludes.add("META-INF/**")
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configureHiltAndroid()

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))

    implementation(findLibrary("androidx.navigation.compose"))
    implementation(findLibrary("androidx.hilt.navigation.compose"))

    implementation(findLibrary("androidx.lifecycle.runtime.compose"))
    implementation(findLibrary("androidx.lifecycle.viewmodel.compose"))
}