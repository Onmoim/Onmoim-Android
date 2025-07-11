plugins {
    alias(libs.plugins.onmoim.android.library)
}

android {
    namespace = "com.onmoim.core.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}