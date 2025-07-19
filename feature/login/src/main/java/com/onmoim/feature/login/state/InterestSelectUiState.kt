package com.onmoim.feature.login.state

import com.onmoim.core.data.model.Category

sealed class InterestSelectUiState {
    data class Success(val categories: List<Category>) : InterestSelectUiState()
    data class Error(val t: Throwable) : InterestSelectUiState()
    data object Loading : InterestSelectUiState()
}