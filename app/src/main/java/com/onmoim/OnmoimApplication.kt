package com.onmoim

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OnmoimApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(applicationContext, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}