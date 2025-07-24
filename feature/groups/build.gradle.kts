plugins {
    alias(libs.plugins.onmoim.android.feature)
    alias(libs.plugins.onmoim.kotlin.library.serialization)
}

android {
    namespace = "com.onmoim.feature.groups"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.util)

    implementation(projects.feature.location)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.paging.runtimeKtx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.naverMap)
    implementation(libs.playServicesLocation)
}