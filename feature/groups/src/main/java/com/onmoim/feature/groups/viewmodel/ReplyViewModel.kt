package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.onmoim.core.data.repository.PostRepository
import com.onmoim.feature.groups.state.ReplyEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ReplyViewModel.Factory::class)
class ReplyViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    @Assisted("postId") private val postId: Int,
    @Assisted("commentId") private val commentId: Int,
    private val postRepository: PostRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("groupId") groupId: Int,
            @Assisted("postId") postId: Int,
            @Assisted("commentId") commentId: Int
        ): ReplyViewModel
    }

    private val _replyState = MutableStateFlow("")
    val replyState = _replyState.asStateFlow()

    val commentThreadPagingData = postRepository.getCommentThreadPagingData(
        groupId = groupId,
        postId = postId,
        commentId = commentId
    ).cachedIn(viewModelScope)

    private val _event = Channel<ReplyEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    fun onReplyChange(value: String) {
        _replyState.value = value
    }

    fun writeReply() {
        viewModelScope.launch {
            val reply = replyState.value

            try {
                postRepository.writeReply(
                    groupId = groupId,
                    postId = postId,
                    commentId = commentId,
                    content = reply
                )
                _replyState.value = ""
                _event.send(ReplyEvent.WriteReplySuccess)
            } catch (e: Exception) {
                Log.e("ReplyViewModel", "writeReply error", e)
                _event.send(ReplyEvent.WriteReplyFailure(e))
            }
        }
    }
}