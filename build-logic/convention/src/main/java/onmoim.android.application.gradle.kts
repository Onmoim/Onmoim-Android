import com.onmoim.configureAndroidJUnit
import com.onmoim.configureHiltAndroid
import com.onmoim.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureAndroidJUnit()