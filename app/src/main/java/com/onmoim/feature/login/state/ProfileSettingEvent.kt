package com.onmoim.feature.login.state

sealed class ProfileSettingEvent {
    data object Loading: ProfileSettingEvent()
    data object ProfileSettingSuccess: ProfileSettingEvent()
    data class ProfileSettingFailed(val t: Throwable): ProfileSettingEvent()
}