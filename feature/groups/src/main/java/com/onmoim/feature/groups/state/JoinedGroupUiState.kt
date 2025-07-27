package com.onmoim.feature.groups.state

import com.onmoim.core.data.model.Group

sealed class JoinedGroupUiState {
    data object Loading: JoinedGroupUiState()
    data class Success(val data: List<Group>): JoinedGroupUiState()
    data class Error(val t: Throwable): JoinedGroupUiState()
}