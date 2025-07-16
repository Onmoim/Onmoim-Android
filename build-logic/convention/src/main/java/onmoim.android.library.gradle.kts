import com.onmoim.configureHiltAndroid
import com.onmoim.configureAndroidJUnit
import com.onmoim.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureHiltAndroid()
configureAndroidJUnit()