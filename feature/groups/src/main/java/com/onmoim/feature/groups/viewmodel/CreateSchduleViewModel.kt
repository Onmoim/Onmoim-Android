package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import com.onmoim.feature.groups.constant.ScheduleType
import com.onmoim.feature.groups.state.CreateScheduleUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime

@HiltViewModel(assistedFactory = CreateScheduleViewModel.Factory::class)
class CreateScheduleViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int): CreateScheduleViewModel
    }

    private val _uiState = MutableStateFlow(CreateScheduleUiState())
    val uiState = _uiState.asStateFlow()

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

    }
}