package com.onmoim.feature.home.state

import com.onmoim.core.data.model.HomeGroup

sealed class HomeRecommendGroupUiState {
    data object Loading: HomeRecommendGroupUiState()
    data class Success(
        val similarGroups: List<HomeGroup>,
        val nearbyGroups: List<HomeGroup>
    ): HomeRecommendGroupUiState()
    data class Error(val error: Throwable): HomeRecommendGroupUiState()
}