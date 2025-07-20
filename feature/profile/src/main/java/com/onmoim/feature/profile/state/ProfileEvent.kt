package com.onmoim.feature.profile.state

sealed class ProfileEvent {
    data class WithdrawalError(val t: Throwable): ProfileEvent()
}