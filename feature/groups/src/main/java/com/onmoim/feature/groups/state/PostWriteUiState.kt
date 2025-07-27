package com.onmoim.feature.groups.state

import com.onmoim.feature.groups.constant.BoardType

data class PostWriteUiState(
    val boardType: BoardType? = null,
    val title: String = "",
    val content: String = "",
    val imagePaths: List<String> = emptyList(),
    val isLoading: Boolean = false
) {
    fun isValid() = boardType != null &&
            title.isNotBlank() &&
            content.isNotBlank()
}