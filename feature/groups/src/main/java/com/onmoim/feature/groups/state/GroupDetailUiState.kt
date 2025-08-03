package com.onmoim.feature.groups.state

import com.onmoim.core.data.model.GroupDetail

sealed class GroupDetailUiState {
    data object Loading : GroupDetailUiState()
    data class Success(val groupDetail: GroupDetail, val userId: Int) : GroupDetailUiState()
    data class Error(val t: Throwable) : GroupDetailUiState()
}