package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.onmoim.core.data.repository.PostRepository
import com.onmoim.feature.groups.state.PostDetailEvent
import com.onmoim.feature.groups.state.PostDetailUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostDetailViewModel.Factory::class)
class PostDetailViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    @Assisted("postId") private val postId: Int,
    private val postRepository: PostRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("groupId") groupId: Int,
            @Assisted("postId") postId: Int
        ): PostDetailViewModel
    }

    private val _postDetailUiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val postDetailUiState = _postDetailUiState.asStateFlow()

    val commentPagingData =
        postRepository.getCommentPagingData(groupId, postId).cachedIn(viewModelScope)

    private val _event = Channel<PostDetailEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _commentState = MutableStateFlow("")
    val commentState = _commentState.asStateFlow()

    init {
        fetchPostDetail()
    }

    fun fetchPostDetail() {
        viewModelScope.launch {
            postRepository.getPost(groupId, postId).catch {
                _postDetailUiState.value = PostDetailUiState.Error(it)
            }.collectLatest {
                _postDetailUiState.value = PostDetailUiState.Success(it)
            }
        }
    }

    fun likePostToggle() {
        val post = (_postDetailUiState.value as? PostDetailUiState.Success)?.post ?: return
        val copyPost = post.copy(
            isLiked = !post.isLiked,
            likeCount = post.likeCount + if (post.isLiked) -1 else 1
        )
        _postDetailUiState.value = PostDetailUiState.Success(copyPost)

        viewModelScope.launch {
            postRepository.likePost(groupId, postId).onFailure {
                _postDetailUiState.value = PostDetailUiState.Success(post)
                _event.send(PostDetailEvent.PostLikeFailure(it))
            }
        }
    }

    fun onCommentChange(value: String) {
        _commentState.value = value
    }

    fun writeComment() {
        val comment = _commentState.value
        if (comment.isBlank()) return

        viewModelScope.launch {
            postRepository.writeComment(groupId, postId, comment).onFailure {
                Log.e("PostDetailViewModel", "writeComment error", it)
                _event.send(PostDetailEvent.CommentWriteFailure(it))
            }.onSuccess {
                _event.send(PostDetailEvent.CommentWriteSuccess)
            }
        }
    }
}