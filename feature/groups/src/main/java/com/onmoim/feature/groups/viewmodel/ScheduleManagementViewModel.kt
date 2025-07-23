package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import com.onmoim.feature.groups.state.ScheduleManagementEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel(assistedFactory = ScheduleManagementViewModel.Factory::class)
class ScheduleManagementViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): ScheduleManagementViewModel
    }

    private val _event = Channel<ScheduleManagementEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun deleteMeeting(meetingId: Int) {

    }
}