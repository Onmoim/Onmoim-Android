plugins {
    alias(libs.plugins.onmoim.android.library)
    alias(libs.plugins.onmoim.android.hilt)
}

android {
    namespace = "com.onmoim.core.domain"
}

dependencies {
    implementation(projects.core.data)
}