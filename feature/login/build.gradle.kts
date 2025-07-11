plugins {
    alias(libs.plugins.onmoim.android.feature)
    alias(libs.plugins.onmoim.kotlin.library.serialization)
}

android {
    namespace = "com.onmoim.feature.login"
}

dependencies {
    implementation(projects.core.helper)
    implementation(projects.core.domain)

    implementation(projects.feature.home)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}