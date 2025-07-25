plugins {
    alias(libs.plugins.onmoim.android.feature)
    alias(libs.plugins.onmoim.kotlin.library.serialization)
}

android {
    namespace = "com.onmoim.feature.home"
}

dependencies {
    implementation(projects.feature.groups)

    implementation(libs.androidx.paging.runtimeKtx)
    implementation(libs.androidx.paging.compose)
}