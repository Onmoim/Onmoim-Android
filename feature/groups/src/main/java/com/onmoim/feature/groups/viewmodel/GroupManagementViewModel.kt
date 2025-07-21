package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import com.onmoim.core.data.model.ActiveStatistics
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.feature.groups.constant.GroupManagementTab
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = GroupManagementViewModel.Factory::class)
class GroupManagementViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    private val groupRepository: GroupRepository
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): GroupManagementViewModel
    }

    private val _selectedTabState = MutableStateFlow(GroupManagementTab.ACTIVE_STATUS)
    val selectedTabState = _selectedTabState.asStateFlow()

    private val _activeStatisticsState = MutableStateFlow<ActiveStatistics?>(null)
    val activeStatisticsState = _activeStatisticsState.asStateFlow()

    fun onTabChange(tab: GroupManagementTab) {
        _selectedTabState.value = tab
    }
}