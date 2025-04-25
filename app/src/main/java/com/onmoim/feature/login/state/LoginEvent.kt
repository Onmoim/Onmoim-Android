package com.onmoim.feature.login.state

sealed class LoginEvent {
    data object NavigateToHome: LoginEvent()
    data object NavigateToSignUp: LoginEvent()
    data class ShowErrorDialog(val t: Throwable? = null): LoginEvent()
}