import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.gms.googleServices)
}

android {
    namespace = "com.onmoim"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.onmoim"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = getLocalPropertyValue("kakao.native.app.key")

        buildConfigField("String", "BASE_URL", "\"https://onmoim.store\"")
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
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("releaseKey")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

fun getLocalPropertyValue(key: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key)
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashScreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.paging.runtimeKtx)
    implementation(libs.androidx.paging.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.okHttp)
    implementation(libs.okHttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)

    implementation(libs.coil.compose)
    implementation(libs.kakao)

    implementation(libs.kotlinx.serialization.json)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}