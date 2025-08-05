package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.onmoim.core.data.model.Comment
import com.onmoim.core.data.repository.PostRepository
import com.onmoim.core.domain.usecase.GetUserIdUseCase
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostDetailViewModel.Factory::class)
class PostDetailViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    @Assisted("postId") private val postId: Int,
    private val postRepository: PostRepository,
    private val getUserIdUseCase: GetUserIdUseCase
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

    private val commentUpdateCache = MutableStateFlow<Map<Int, Comment>>(emptyMap())
    private val commentDeleteCache = MutableStateFlow<Set<Int>>(emptySet())
    val commentPagingData = combine(
        postRepository.getCommentPagingData(groupId, postId),
        commentUpdateCache,
        commentDeleteCache
    ) { pagingData, updateCache, deleteCache ->
        pagingData.filter {
            it.id !in deleteCache
        }.map {
            updateCache[it.id] ?: it
        }
    }.cachedIn(viewModelScope)

    private val _event = Channel<PostDetailEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _commentState = MutableStateFlow("")
    val commentState = _commentState.asStateFlow()

    private val _userIdState = MutableStateFlow<Int?>(null)
    val userIdState = _userIdState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchPostDetail()
        fetchUserId()
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

    private fun fetchUserId() {
        viewModelScope.launch {
            _userIdState.value = getUserIdUseCase()
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
                _commentState.value = ""
                _event.send(PostDetailEvent.CommentWriteSuccess)
            }
        }
    }

    fun updateComment(commentId: Int, comment: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                postRepository.updateComment(groupId, postId, commentId, comment)
                _event.send(PostDetailEvent.CommentUpdateSuccess)
            } catch (e: Exception) {
                Log.e("PostDetailViewModel", "updateComment error", e)
                _event.send(PostDetailEvent.CommentUpdateFailure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                postRepository.deleteComment(groupId, postId, commentId)
                _event.send(PostDetailEvent.CommentDeleteSuccess)
            } catch (e: Exception) {
                Log.e("PostDetailViewModel", "deleteComment error", e)
                _event.send(PostDetailEvent.CommentDeleteFailure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearCache() {
        commentUpdateCache.value = emptyMap()
        commentDeleteCache.value = emptySet()
    }
}