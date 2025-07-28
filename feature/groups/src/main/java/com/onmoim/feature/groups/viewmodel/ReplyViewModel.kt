package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import com.onmoim.core.data.repository.PostRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = ReplyViewModel.Factory::class)
class ReplyViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    @Assisted("postId") private val postId: Int,
    @Assisted("commentId") private val commentId: Int,
    private val postRepository: PostRepository
): ViewModel() {

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

    fun onReplyChange(value: String) {
        _replyState.value = value
    }

    fun writeReply() {

    }
}