package com.onmoim.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.feature.home.constant.HomeGroupType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel(assistedFactory = GroupMoreViewModel.Factory::class)
class GroupMoreViewModel @AssistedInject constructor(
    @Assisted("homeGroupType") val homeGroupType: HomeGroupType,
    private val groupRepository: GroupRepository
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("homeGroupType") homeGroupType: HomeGroupType
        ): GroupMoreViewModel
    }
}