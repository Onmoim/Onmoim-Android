package com.onmoim.feature.login.state

import com.onmoim.core.data.model.Interest

sealed class InterestSelectUiState {
    data class Success(val categories: List<Interest>) : InterestSelectUiState()
    data class Error(val t: Throwable) : InterestSelectUiState()
    data object Loading : InterestSelectUiState()
}