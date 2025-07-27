package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.repository.MeetingRepository
import com.onmoim.feature.groups.constant.ScheduleType
import com.onmoim.feature.groups.state.CreateScheduleEvent
import com.onmoim.feature.groups.state.CreateScheduleUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@HiltViewModel(assistedFactory = CreateScheduleViewModel.Factory::class)
class CreateScheduleViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): CreateScheduleViewModel
    }

    private val _uiState = MutableStateFlow(CreateScheduleUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<CreateScheduleEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    fun onImageChange(imagePath: String) {
        _uiState.update { state ->
            state.copy(
                imagePath = imagePath
            )
        }
    }

    fun onScheduleTypeChange(type: ScheduleType) {
        _uiState.update { state ->
            state.copy(
                type = type
            )
        }
    }

    fun onScheduleNameChange(name: String) {
        _uiState.update { state ->
            state.copy(
                name = name
            )
        }
    }

    fun onStartDateChange(date: LocalDate) {
        val nowTime = LocalTime.now()
        val selectedTime = uiState.value.startTime

        if (selectedTime != null && selectedTime.isBefore(nowTime) && date == LocalDate.now()) {
            _uiState.update { state ->
                state.copy(
                    startTime = null
                )
            }
        }

        _uiState.update { state ->
            state.copy(
                startDate = date
            )
        }
    }

    fun onStartTimeChange(time: LocalTime) {
        _uiState.update { state ->
            state.copy(
                startTime = time
            )
        }
    }

    fun onPlaceChange(place: String, latitude: Double?, longitude: Double?) {
        _uiState.update { state ->
            state.copy(
                place = place,
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    fun onCostChange(cost: Long?) {
        _uiState.update { state ->
            state.copy(
                cost = cost
            )
        }
    }

    fun onCapacityChange(capacity: Int?) {
        _uiState.update { state ->
            state.copy(
                capacity = capacity
            )
        }
    }

    fun createSchedule() {
        _uiState.update { state ->
            state.copy(
                isLoading = true
            )
        }

        val type = when (uiState.value.type) {
            ScheduleType.REGULAR -> "REGULAR"
            ScheduleType.LIGHTNING -> "FLASH"
            null -> "REGULAR"
        }
        val startDateTime = LocalDateTime.of(uiState.value.startDate, uiState.value.startTime)
        val latitude = requireNotNull(uiState.value.latitude) {
            "latitude는 null이면 안됨"
        }
        val longitude = requireNotNull(uiState.value.longitude) {
            "longitude는 null이면 안됨"
        }
        val cost = requireNotNull(uiState.value.cost) {
            "cost는 null이면 안됨"
        }
        val capacity = requireNotNull(uiState.value.capacity) {
            "capacity는 null이면 안됨"
        }

        viewModelScope.launch {
            meetingRepository.createMeeting(
                groupId = groupId,
                type = type,
                title = uiState.value.name,
                startDateTime = startDateTime,
                placeName = uiState.value.place,
                latitude = latitude,
                longitude = longitude,
                capacity = capacity,
                cost = cost,
                imagePath = uiState.value.imagePath
            ).onFailure {
                Log.e("CreateScheduleViewModel", "createSchedule error", it)
                _uiState.update { state ->
                    state.copy(
                        isLoading = false
                    )
                }
                _event.send(CreateScheduleEvent.CreateFailure(it))
            }.onSuccess {
                _event.send(CreateScheduleEvent.CreateSuccess)
            }
        }
    }
}