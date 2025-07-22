package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.feature.groups.state.GroupDetailEvent
import com.onmoim.feature.groups.state.GroupDetailUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GroupDetailViewModel.Factory::class)
class GroupDetailViewModel @AssistedInject constructor(
    @Assisted("id") private val id: Int,
    private val groupRepository: GroupRepository
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(@Assisted("id") id: Int): GroupDetailViewModel
    }

    private val _groupDetailUiState =
        MutableStateFlow<GroupDetailUiState>(GroupDetailUiState.Loading)
    val groupDetailUiState = _groupDetailUiState.asStateFlow()

    private val _event = Channel<GroupDetailEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchGroupDetail()
    }

    fun fetchGroupDetail(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                _groupDetailUiState.value = GroupDetailUiState.Loading
            }

            groupRepository.getGroupDetail(id).catch {
                _groupDetailUiState.value = GroupDetailUiState.Error(it)
            }.collectLatest {
                _groupDetailUiState.value = GroupDetailUiState.Success(it)
            }
        }
    }

    fun leaveGroup() {
        viewModelScope.launch {
            _isLoading.value = true

            groupRepository.leaveGroup(id).onFailure {
                Log.e("GroupDetailViewModel", "leaveGroup error", it)
                _isLoading.value = false
                _event.send(GroupDetailEvent.LeaveGroupFailure(it))
            }.onSuccess {
                _isLoading.value = false
                _event.send(GroupDetailEvent.LeaveGroupSuccess)
            }
        }
    }

    fun deleteGroup() {
        viewModelScope.launch {
            _isLoading.value = true

            groupRepository.deleteGroup(id).onFailure {
                Log.e("GroupDetailViewModel", "deleteGroup error", it)
                _isLoading.value = false
                _event.send(GroupDetailEvent.DeleteGroupFailure(it))
            }.onSuccess {
                _isLoading.value = false
                _event.send(GroupDetailEvent.DeleteGroupSuccess)
            }
        }
    }

    fun favoriteGroup(favorite: Boolean) {
        val groupDetail = (_groupDetailUiState.value as? GroupDetailUiState.Success)?.groupDetail ?: return

        viewModelScope.launch {
            _groupDetailUiState.value = GroupDetailUiState.Success(
                groupDetail.copy(
                    isFavorite = !favorite
                )
            )

            groupRepository.favoriteGroup(id).onFailure {
                Log.e("GroupDetailViewModel", "favoriteGroup error", it)
                _groupDetailUiState.value = GroupDetailUiState.Success(
                    groupDetail.copy(
                        isFavorite = favorite
                    )
                )
                _event.send(GroupDetailEvent.FavoriteGroupFailure(it))
            }
        }
    }

    fun joinGroup() {
        // TODO: 모임 가입
    }

    fun attendMeeting(meetingId: Int) {
        // TODO: 일정 참가
    }
}