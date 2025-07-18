package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = GroupDetailViewModel.Factory::class)
class GroupDetailViewModel @AssistedInject constructor(
    @Assisted("id") private val id: Int
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(@Assisted("id") id: Int): GroupDetailViewModel
    }


}