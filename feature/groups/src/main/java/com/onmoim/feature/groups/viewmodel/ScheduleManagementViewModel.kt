package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.onmoim.core.data.repository.MeetingRepository
import com.onmoim.feature.groups.state.ScheduleManagementEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ScheduleManagementViewModel.Factory::class)
class ScheduleManagementViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): ScheduleManagementViewModel
    }

    private val _event = Channel<ScheduleManagementEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val deletedMeetingIds = MutableStateFlow<Set<Int>>(emptySet())
    val meetingPagingData = combine(
        meetingRepository.getMeetingPagingData(groupId),
        deletedMeetingIds
    ) { pagingData, deletedMeetingIds ->
        pagingData.filter { meeting ->
            meeting.id !in deletedMeetingIds
        }
    }.cachedIn(viewModelScope)

    fun deleteMeeting(meetingId: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            meetingRepository.deleteMeeting(groupId, meetingId).onFailure {
                _isLoading.value = false
                _event.send(ScheduleManagementEvent.DeleteFailure(it))
            }.onSuccess {
                deletedMeetingIds.value += meetingId
                _isLoading.value = false
                _event.send(ScheduleManagementEvent.DeleteSuccess)
            }
        }
    }
}