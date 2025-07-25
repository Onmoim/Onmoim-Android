package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import com.onmoim.core.data.repository.PostRepository
import com.onmoim.feature.groups.constant.BoardType
import com.onmoim.feature.groups.state.PostWriteUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel(assistedFactory = PostWriteViewModel.Factory::class)
class PostWriteViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    private val postRepository: PostRepository
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): PostWriteViewModel
    }

    private val _uiState = MutableStateFlow(PostWriteUiState())
    val uiState = _uiState.asStateFlow()

    fun onBoardTypeChange(value: BoardType) {
        _uiState.update { state ->
            state.copy(boardType = value)
        }
    }

    fun onTitleChange(value: String) {
        _uiState.update { state ->
            state.copy(title = value)
        }
    }

    fun onContentChange(value: String) {
        _uiState.update { state ->
            state.copy(content = value)
        }
    }

    fun addImages(imagePaths: List<String>) {
        _uiState.update { state ->
            state.copy(imagePaths = state.imagePaths + imagePaths)
        }
    }

    fun deleteImagePath(value: String) {
        _uiState.update { state ->
            state.copy(imagePaths = state.imagePaths - value)
        }
    }

    fun writePost() {

    }
}