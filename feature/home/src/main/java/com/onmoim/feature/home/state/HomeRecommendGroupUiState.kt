package com.onmoim.feature.home.state

import com.onmoim.core.data.model.Group

sealed class HomeRecommendGroupUiState {
    data object Loading: HomeRecommendGroupUiState()
    data class Success(
        val similarGroups: List<Group>,
        val nearbyGroups: List<Group>
    ): HomeRecommendGroupUiState()
    data class Error(val error: Throwable): HomeRecommendGroupUiState()
}