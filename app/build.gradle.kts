import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.onmoim.filterProject

plugins {
    alias(libs.plugins.onmoim.android.application)
    alias(libs.plugins.onmoim.android.compose)
    alias(libs.plugins.gms.googleServices)
}

android {
    namespace = "com.onmoim"

    defaultConfig {
        applicationId = "com.onmoim"
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = getLocalPropertyValue("kakao.native.app.key")
        manifestPlaceholders["NAVER_MAP_CLIENT_ID"] = getLocalPropertyValue("naver.map.client.id")

        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "\"${getLocalPropertyValue("kakao.native.app.key")}\""
        )
    }

    signingConfigs {
        create("debugKey") {
            storeFile = file("../keystore/onmoim_debug_key.jks")
            storePassword = getLocalPropertyValue("store.password")
            keyAlias = getLocalPropertyValue("debug.key.alias")
            keyPassword = getLocalPropertyValue("key.password")
        }
        create("releaseKey") {
            storeFile = file("../keystore/onmoim_release_key.jks")
            storePassword = getLocalPropertyValue("store.password")
            keyAlias = getLocalPropertyValue("release.key.alias")
            keyPassword = getLocalPropertyValue("key.password")
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debugKey")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("releaseKey")
        }
    }
}

fun getLocalPropertyValue(key: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashScreen)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation(libs.kakao)

    rootProject.subprojects.filterProject {
        implementation(it)
    }
}