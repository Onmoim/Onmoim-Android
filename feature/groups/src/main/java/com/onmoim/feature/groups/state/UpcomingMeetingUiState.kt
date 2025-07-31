package com.onmoim.feature.groups.state

import com.onmoim.core.data.model.Meeting

sealed class UpcomingMeetingUiState {
    object Loading: UpcomingMeetingUiState()
    data class Success(val data: List<Meeting>): UpcomingMeetingUiState()
    data class Error(val t: Throwable): UpcomingMeetingUiState()
}