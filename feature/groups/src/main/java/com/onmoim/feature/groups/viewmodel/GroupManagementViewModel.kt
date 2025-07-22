package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.onmoim.core.data.model.ActiveStatistics
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.core.domain.usecase.GetUserIdUseCase
import com.onmoim.feature.groups.constant.GroupManagementTab
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GroupManagementViewModel.Factory::class)
class GroupManagementViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    private val groupRepository: GroupRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): GroupManagementViewModel
    }

    private val _event = Channel<GroupManagementEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _selectedTabState = MutableStateFlow(GroupManagementTab.ACTIVE_STATUS)
    val selectedTabState = _selectedTabState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _activeStatisticsState = MutableStateFlow<ActiveStatistics?>(null)
    val activeStatisticsState = _activeStatisticsState.asStateFlow()

    private val banMemberCache = MutableStateFlow<Set<Int>>(emptySet())
    val groupMemberPagingData = combine(
        groupRepository.getGroupMemberPagingData(groupId),
        banMemberCache
    ) { pagingData, banMember ->
        pagingData.filter { it.id !in banMember }
    }.cachedIn(viewModelScope)

    private val _userIdState = MutableStateFlow<Int?>(null)
    val userIdState = _userIdState.asStateFlow()

    init {
        fetchActiveStatistics()
        fetchUserId()
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

    private fun fetchUserId() {
        viewModelScope.launch {
            _userIdState.value = getUserIdUseCase()
        }
    }

    fun banMember(memberId: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            groupRepository.banMember(groupId, memberId).onFailure {
                Log.e("GroupManagementViewModel", "banMember error", it)
                _isLoading.value = false
                _event.send(GroupManagementEvent.BanFailure(it))
            }.onSuccess {
                banMemberCache.value = banMemberCache.value + memberId
                _isLoading.value = false
                _event.send(GroupManagementEvent.BanSuccess)
            }
        }
    }
}

sealed class GroupManagementEvent {
    data object BanSuccess: GroupManagementEvent()
    data class BanFailure(val t: Throwable): GroupManagementEvent()
}