package com.onmoim.feature.profile.state

sealed class ProfileEditEvent {
    data object UpdateSuccess: ProfileEditEvent()
    data class UpdateFailure(val t: Throwable): ProfileEditEvent()
}