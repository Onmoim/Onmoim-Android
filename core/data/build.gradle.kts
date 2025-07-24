plugins {
    alias(libs.plugins.onmoim.android.library)
    alias(libs.plugins.onmoim.kotlin.library.serialization)
}

android {
    namespace = "com.onmoim.core.data"
}

dependencies {
    implementation(projects.core.dispatcher)
    implementation(projects.core.datastore)
    implementation(projects.core.network)

    implementation(libs.retrofit)
    implementation(libs.androidx.paging.runtimeKtx)
}