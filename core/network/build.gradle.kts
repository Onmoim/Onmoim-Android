import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.onmoim.android.library)
    alias(libs.plugins.onmoim.android.hilt)
    alias(libs.plugins.onmoim.kotlin.library.serialization)
}

android {
    namespace = "com.onmoim.core.network"

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://onmoim.store\"")
        buildConfigField(
            "String",
            "KAKAO_REST_API_KEY",
            "\"${getLocalPropertyValue("kakao.rest.api.key")}\""
        )
    }
}

fun getLocalPropertyValue(key: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key)
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