package com.onmoim.feature.groups.state

import com.onmoim.core.data.model.Post

sealed class PostDetailUiState {
    data object Loading : PostDetailUiState()
    data class Success(val post: Post) : PostDetailUiState()
    data class Error(val t: Throwable) : PostDetailUiState()
}