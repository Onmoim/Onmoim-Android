package com.onmoim.feature.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.core.data.repository.UserRepository
import com.onmoim.feature.profile.constant.GroupType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = GroupListViewModel.Factory::class)
class GroupListViewModel @AssistedInject constructor(
    @Assisted("groupType") val groupType: GroupType,
    groupRepository: GroupRepository,
    userRepository: UserRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupType") groupType: GroupType): GroupListViewModel
    }

    val groupPagingData = when(groupType) {
        GroupType.FAVORITE -> groupRepository.getFavoriteGroupPagingData()
        GroupType.RECENT -> userRepository.getRecentGroupPagingData()
        GroupType.JOIN -> groupRepository.getJoinedGroupPagingData()
    }
}