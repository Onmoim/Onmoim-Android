package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.constant.BoardCategory
import com.onmoim.core.data.repository.PostRepository
import com.onmoim.feature.groups.constant.BoardType
import com.onmoim.feature.groups.state.PostWriteEvent
import com.onmoim.feature.groups.state.PostWriteUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostWriteViewModel.Factory::class)
class PostWriteViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    private val postRepository: PostRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): PostWriteViewModel
    }

    private val _uiState = MutableStateFlow(PostWriteUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PostWriteEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

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

    fun createPost() {
        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        val boardCategory = when (uiState.value.boardType) {
            BoardType.NOTICE -> BoardCategory.NOTICE
            BoardType.INTRO -> BoardCategory.INTRODUCTION
            BoardType.REVIEW -> BoardCategory.REVIEW
            BoardType.FREE -> BoardCategory.FREE
            null -> return
        }

        viewModelScope.launch {
            postRepository.createPost(
                groupId = groupId,
                boardCategory = boardCategory,
                title = uiState.value.title,
                content = uiState.value.content,
                imagePaths = uiState.value.imagePaths
            ).onFailure {
                Log.e("PostWriteViewModel", "createPost error", it)
                _uiState.update { state ->
                    state.copy(isLoading = false)
                }
                _event.send(PostWriteEvent.CreatePostFailure(it))
            }.onSuccess {
                _event.send(PostWriteEvent.CreatePostSuccess)
            }
        }
    }
}