package com.onmoim.feature.home.state

import com.onmoim.core.data.model.Group

sealed class HomePopularGroupUiState {
    data object Loading: HomePopularGroupUiState()
    data class Success(
        val nearbyGroups: List<Group>,
        val activeGroups: List<Group>
    ): HomePopularGroupUiState()
    data class Error(val error: Throwable): HomePopularGroupUiState()
}