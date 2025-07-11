plugins {
    alias(libs.plugins.onmoim.android.library)
    alias(libs.plugins.onmoim.android.hilt)
    alias(libs.plugins.onmoim.kotlin.library.serialization)
}

android {
    namespace = "com.onmoim.core.network"

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://onmoim.store\"")
    }
}

dependencies {
    implementation(projects.core.event)
    implementation(projects.core.dispatcher)
    implementation(projects.core.datastore)

    implementation(libs.okHttp)
    implementation(libs.okHttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
}