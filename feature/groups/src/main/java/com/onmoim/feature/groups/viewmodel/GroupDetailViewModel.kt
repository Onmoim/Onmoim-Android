package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.onmoim.core.data.constant.JoinGroupResult
import com.onmoim.core.data.constant.JoinMeetingResult
import com.onmoim.core.data.constant.LeaveMeetingResult
import com.onmoim.core.data.constant.PostType
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.core.data.repository.MeetingRepository
import com.onmoim.core.data.repository.PostRepository
import com.onmoim.feature.groups.constant.GroupDetailPostType
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
    @Assisted("groupId") private val groupId: Int,
    private val groupRepository: GroupRepository,
    private val meetingRepository: MeetingRepository,
    private val postRepository: PostRepository
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): GroupDetailViewModel
    }

    private val _groupDetailUiState =
        MutableStateFlow<GroupDetailUiState>(GroupDetailUiState.Loading)
    val groupDetailUiState = _groupDetailUiState.asStateFlow()

    private val _event = Channel<GroupDetailEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _postFilterState = MutableStateFlow(GroupDetailPostType.ALL)
    val postFilterState = _postFilterState.asStateFlow()

    val allPostPagingData =
        postRepository.getPostPagingData(groupId, PostType.ALL).cachedIn(viewModelScope)
    val noticePostPagingData =
        postRepository.getPostPagingData(groupId, PostType.NOTICE).cachedIn(viewModelScope)
    val introPostPagingData =
        postRepository.getPostPagingData(groupId, PostType.INTRODUCTION).cachedIn(viewModelScope)
    val reviewPostPagingData =
        postRepository.getPostPagingData(groupId, PostType.REVIEW).cachedIn(viewModelScope)
    val freePostPagingData =
        postRepository.getPostPagingData(groupId, PostType.FREE).cachedIn(viewModelScope)

    init {
        fetchGroupDetail()
    }

    fun fetchGroupDetail(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                _isLoading.value = true
            }

            groupRepository.getGroupDetail(groupId).catch {
                Log.e("GroupDetailViewModel", "fetchGroupDetail error", it)
                if (refresh) {
                    _isLoading.value = false
                }
                _groupDetailUiState.value = GroupDetailUiState.Error(it)
            }.collectLatest {
                if (refresh) {
                    _isLoading.value = false
                }
                _groupDetailUiState.value = GroupDetailUiState.Success(it)
            }
        }
    }

    fun leaveGroup() {
        viewModelScope.launch {
            _isLoading.value = true

            groupRepository.leaveGroup(groupId).onFailure {
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

            groupRepository.deleteGroup(groupId).onFailure {
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
        val groupDetail =
            (_groupDetailUiState.value as? GroupDetailUiState.Success)?.groupDetail ?: return

        viewModelScope.launch {
            _groupDetailUiState.value = GroupDetailUiState.Success(
                groupDetail.copy(
                    isFavorite = !favorite
                )
            )

            groupRepository.favoriteGroup(groupId).onFailure {
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
        _isLoading.value = true

        viewModelScope.launch {
            groupRepository.joinGroup(groupId).onFailure {
                Log.e("GroupDetailViewModel", "joinGroup error", it)
                _isLoading.value = false
                _event.send(GroupDetailEvent.JoinGroupFailure(it))
            }.onSuccess { result ->
                when (result) {
                    JoinGroupResult.SUCCESS -> {
                        fetchGroupDetail(true)
                        _event.send(GroupDetailEvent.JoinGroupSuccess)
                    }

                    JoinGroupResult.BANNED -> {
                        _isLoading.value = false
                        _event.send(GroupDetailEvent.JoinGroupBanned)
                    }

                    JoinGroupResult.NOT_FOUND -> {
                        _isLoading.value = false
                        _event.send(GroupDetailEvent.JoinGroupNotFound)
                    }

                    JoinGroupResult.OVER_CAPACITY -> {
                        _isLoading.value = false
                        _event.send(GroupDetailEvent.JoinGroupOverCapacity)
                    }
                }

            }
        }
    }

    fun attendMeeting(meetingId: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            meetingRepository.joinMeeting(groupId, meetingId).onFailure {
                Log.e("GroupDetailViewModel", "attendMeeting error", it)
                _isLoading.value = false
                _event.send(GroupDetailEvent.AttendMeetingFailure(it))
            }.onSuccess { result ->
                fetchGroupDetail(refresh = true)

                when (result) {
                    JoinMeetingResult.SUCCESS -> {
                        _event.send(GroupDetailEvent.AttendMeetingSuccess)
                    }

                    JoinMeetingResult.NOT_FOUND -> {
                        _event.send(GroupDetailEvent.MeetingNotFound)
                    }

                    JoinMeetingResult.OVER_CAPACITY -> {
                        _event.send(GroupDetailEvent.AttendMeetingOverCapacity)
                    }
                }
            }
        }
    }

    fun leaveMeeting(meetingId: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            meetingRepository.leaveMeeting(groupId, meetingId).onFailure {
                Log.e("GroupDetailViewModel", "leaveMeeting error", it)
                _isLoading.value = false
                _event.send(GroupDetailEvent.LeaveMeetingFailure(it))
            }.onSuccess { result ->
                fetchGroupDetail(refresh = true)

                when (result) {
                    LeaveMeetingResult.SUCCESS -> {
                        _event.send(GroupDetailEvent.LeaveMeetingSuccess)
                    }

                    LeaveMeetingResult.NOT_FOUND -> {
                        _event.send(GroupDetailEvent.MeetingNotFound)
                    }
                }
            }
        }
    }

    fun onPostFilterChange(value: GroupDetailPostType) {
        _postFilterState.value = value
    }
}