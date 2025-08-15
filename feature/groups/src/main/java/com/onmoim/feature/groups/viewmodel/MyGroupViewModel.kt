package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.onmoim.core.data.constant.JoinMeetingResult
import com.onmoim.core.data.constant.LeaveMeetingResult
import com.onmoim.core.data.model.Meeting
import com.onmoim.core.data.repository.ChatRepository
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.core.data.repository.MeetingRepository
import com.onmoim.feature.groups.constant.MyGroupTab
import com.onmoim.feature.groups.state.JoinedGroupUiState
import com.onmoim.feature.groups.state.MyGroupEvent
import com.onmoim.feature.groups.state.UpcomingMeetingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MyGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val meetingRepository: MeetingRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _selectedTabState = MutableStateFlow(MyGroupTab.MY_GROUP)
    val selectedTabState = _selectedTabState.asStateFlow()

    private val _joinedGroupUiState =
        MutableStateFlow<JoinedGroupUiState>(JoinedGroupUiState.Loading)
    val joinedGroupUiState = _joinedGroupUiState.asStateFlow()

    private val _selectedDateState = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDateState = _selectedDateState.asStateFlow()

    private val _upcomingMeetingUiState =
        MutableStateFlow<UpcomingMeetingUiState>(UpcomingMeetingUiState.Loading)
    val upcomingMeetingUiState = _upcomingMeetingUiState.asStateFlow()

    private val upcomingMeetingKeyFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyyMMdd")
    private val cachedUpcomingMeeting = mutableMapOf<String, List<Meeting>>()

    private val _event = Channel<MyGroupEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val chatRoomPagingData = chatRepository.getChatRoomPagingData().cachedIn(viewModelScope)

    init {
        fetchJoinedGroup()
        fetchUpcomingMeeting()
    }

    fun onSelectedTabChange(tab: MyGroupTab) {
        _selectedTabState.value = tab
    }

    fun fetchJoinedGroup() {
        viewModelScope.launch {
            groupRepository.getJoinedGroups().catch {
                _joinedGroupUiState.value = JoinedGroupUiState.Error(it)
            }.collectLatest {
                _joinedGroupUiState.value = JoinedGroupUiState.Success(it)
            }
        }
    }

    fun onSelectedDateChange(date: LocalDate) {
        _selectedDateState.value = date
        fetchUpcomingMeeting()
    }

    private fun fetchUpcomingMeeting(refresh: Boolean = false) {
        val selectedDate = selectedDateState.value
        if (refresh) {
            _isLoading.value = true
        } else {
            val key = upcomingMeetingKeyFormatter.format(selectedDate)
            val meetings = cachedUpcomingMeeting[key]
            if (meetings?.isNotEmpty() == true) {
                _upcomingMeetingUiState.value = UpcomingMeetingUiState.Success(meetings)
                return
            }
        }

        viewModelScope.launch {
            meetingRepository.getUpcomingMeetingsByDate(selectedDate).catch {
                Log.e("MyGroupViewModel", "fetchUpcomingMeeting error", it)
                if (refresh) {
                    _isLoading.value = false
                }
                _upcomingMeetingUiState.value = UpcomingMeetingUiState.Error(it)
            }.collectLatest {
                if (refresh) {
                    _isLoading.value = false
                }
                val key = upcomingMeetingKeyFormatter.format(selectedDate)
                cachedUpcomingMeeting[key] = it
                _upcomingMeetingUiState.value = UpcomingMeetingUiState.Success(it)
            }
        }
    }

    fun attendMeeting(meetingId: Int, groupId: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            meetingRepository.joinMeeting(groupId, meetingId).onFailure {
                Log.e("MyGroupViewModel", "attendMeeting error", it)
                _isLoading.value = false
                _event.send(MyGroupEvent.AttendMeetingFailure(it))
            }.onSuccess { result ->
                fetchUpcomingMeeting(true)

                when (result) {
                    JoinMeetingResult.SUCCESS -> {
                        _event.send(MyGroupEvent.AttendMeetingSuccess)
                    }

                    JoinMeetingResult.NOT_FOUND -> {
                        _event.send(MyGroupEvent.MeetingNotFound)
                    }

                    JoinMeetingResult.OVER_CAPACITY -> {
                        _event.send(MyGroupEvent.AttendMeetingOverCapacity)
                    }
                }
            }
        }
    }

    fun leaveMeeting(meetingId: Int, groupId: Int) {
        _isLoading.value = true

        viewModelScope.launch {
            meetingRepository.leaveMeeting(groupId, meetingId).onFailure {
                Log.e("MyGroupViewModel", "leaveMeeting error", it)
                _isLoading.value = false
                _event.send(MyGroupEvent.LeaveMeetingFailure(it))
            }.onSuccess { result ->
                fetchUpcomingMeeting(true)

                when (result) {
                    LeaveMeetingResult.SUCCESS -> {
                        _event.send(MyGroupEvent.LeaveMeetingSuccess)
                    }

                    LeaveMeetingResult.NOT_FOUND -> {
                        _event.send(MyGroupEvent.MeetingNotFound)
                    }
                }
            }
        }
    }
}