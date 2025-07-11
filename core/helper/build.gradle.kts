import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.onmoim.android.library)
    alias(libs.plugins.onmoim.android.hilt)
}

android {
    namespace = "com.onmoim.core.helper"

    defaultConfig {
        buildConfigField(
            "String",
            "GOOGLE_SIGN_IN_SERVER_CLIENT_ID",
            "\"${getLocalPropertyValue("google.sign.in.server.client.id")}\""
        )
    }
}

fun getLocalPropertyValue(key: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key)
}

dependencies {
    implementation(libs.kakao)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.playServicesAuth)
    implementation(libs.googleId)
}