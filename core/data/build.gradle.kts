plugins {
    alias(libs.plugins.onmoim.android.library)
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