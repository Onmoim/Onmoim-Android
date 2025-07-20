plugins {
    alias(libs.plugins.onmoim.android.feature)
    alias(libs.plugins.onmoim.kotlin.library.serialization)
}

android {
    namespace = "com.onmoim.feature.profile"
}

dependencies {
    implementation(projects.feature.groups)
    implementation(projects.core.domain)
    implementation(projects.core.event)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}