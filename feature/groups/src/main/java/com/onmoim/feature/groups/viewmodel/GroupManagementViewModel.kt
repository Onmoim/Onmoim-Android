package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.onmoim.core.data.model.ActiveStatistics
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.feature.groups.constant.GroupManagementTab
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GroupManagementViewModel.Factory::class)
class GroupManagementViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    private val groupRepository: GroupRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): GroupManagementViewModel
    }

    private val _selectedTabState = MutableStateFlow(GroupManagementTab.ACTIVE_STATUS)
    val selectedTabState = _selectedTabState.asStateFlow()

    private val _activeStatisticsState = MutableStateFlow<ActiveStatistics?>(null)
    val activeStatisticsState = _activeStatisticsState.asStateFlow()

    val groupMemberPagingData =
        groupRepository.getGroupMemberPagingData(groupId).cachedIn(viewModelScope)

    init {
        fetchActiveStatistics()
    }

    fun onTabChange(tab: GroupManagementTab) {
        _selectedTabState.value = tab
    }

    fun fetchActiveStatistics() {
        viewModelScope.launch {
            groupRepository.getActiveStatistics(groupId).retryWhen { cause, attempt ->
                delay(1000)
                attempt < 3
            }.catch {
                Log.e("GroupManagementViewModel", "fetchActiveStatistics error", it)
            }.collectLatest {
                _activeStatisticsState.value = it
            }
        }
    }
}