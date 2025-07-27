package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import com.onmoim.core.data.repository.PostRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = PostDetailViewModel.Factory::class)
class PostDetailViewModel @AssistedInject constructor(
    @Assisted("postId") private val postId: Int,
    private val postRepository: PostRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("postId") postId: Int): PostDetailViewModel
    }
}