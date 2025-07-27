plugins {
    alias(libs.plugins.onmoim.android.library)
    alias(libs.plugins.onmoim.android.compose)
}

android {
    namespace = "com.onmoim.core.ui"
}

dependencies {
    implementation(libs.naverMap)
    implementation(libs.playServicesLocation)
}