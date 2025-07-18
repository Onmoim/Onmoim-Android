package com.onmoim.feature.home.state

import com.onmoim.core.data.model.HomeGroup

sealed class HomePopularGroupUiState {
    data object Loading: HomePopularGroupUiState()
    data class Success(
        val nearbyGroups: List<HomeGroup>,
        val activeGroups: List<HomeGroup>
    ): HomePopularGroupUiState()
    data class Error(val error: Throwable): HomePopularGroupUiState()
}