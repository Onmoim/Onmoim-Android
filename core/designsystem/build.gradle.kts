plugins {
    alias(libs.plugins.onmoim.android.library)
    alias(libs.plugins.onmoim.android.compose)
}

android {
    namespace = "com.onmoim.core.designsystem"
}

dependencies {
    implementation(projects.core.ui)

    implementation(libs.androidx.ui.text.google.fonts)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}