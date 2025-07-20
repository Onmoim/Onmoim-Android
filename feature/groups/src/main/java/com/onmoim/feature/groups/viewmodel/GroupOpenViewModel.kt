package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.feature.groups.state.GroupOpenUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GroupOpenViewModel.Factory::class)
class GroupOpenViewModel @AssistedInject constructor(
    @Assisted("categoryId") private val categoryId: Int
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("categoryId") categoryId: Int
        ): GroupOpenViewModel
    }

    private val _uiState = MutableStateFlow(GroupOpenUiState())
    val uiState = _uiState.asStateFlow()

    fun onLocationChange(locationId: Int, locationName: String) {
        _uiState.update { state ->
            state.copy(
                locationId = locationId,
                locationName = locationName
            )
        }
    }

    fun onGroupNameChange(groupName: String) {
        _uiState.update { state ->
            state.copy(
                groupName = groupName
            )
        }
    }

    fun onGroupDescriptionChange(groupDescription: String) {
        _uiState.update { state ->
            state.copy(
                groupDescription = groupDescription
            )
        }
    }

    fun onGroupCapacityChange(groupCapacity: Int?) {
        _uiState.update { state ->
            state.copy(
                groupCapacity = groupCapacity
            )
        }
    }

    fun onClickConfirm() {
        viewModelScope.launch {

        }
    }
}