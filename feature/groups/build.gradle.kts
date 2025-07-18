plugins {
    alias(libs.plugins.onmoim.android.feature)
    alias(libs.plugins.onmoim.kotlin.library.serialization)
}

android {
    namespace = "com.onmoim.feature.groups"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}