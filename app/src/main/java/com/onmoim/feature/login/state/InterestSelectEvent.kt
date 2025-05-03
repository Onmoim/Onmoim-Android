package com.onmoim.feature.login.state

sealed class InterestSelectEvent {
    data class ShowErrorDialog(val t: Throwable? = null): InterestSelectEvent()
    data object Loading: InterestSelectEvent()
    data object NavigateToHome: InterestSelectEvent()
}