package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.feature.groups.state.GroupOpenEvent
import com.onmoim.feature.groups.state.GroupOpenUiState
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GroupOpenViewModel.Factory::class)
class GroupOpenViewModel @AssistedInject constructor(
    @Assisted("categoryId") private val categoryId: Int,
    private val groupRepository: GroupRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("categoryId") categoryId: Int
        ): GroupOpenViewModel
    }

    private val _uiState = MutableStateFlow(GroupOpenUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<GroupOpenEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

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
            val name = uiState.value.groupName
            val description = uiState.value.groupDescription
            val locationId = uiState.value.locationId
            val capacity = uiState.value.groupCapacity ?: return@launch

            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }

            groupRepository.createGroup(
                name = name,
                description = description,
                locationId = locationId,
                categoryId = categoryId,
                capacity = capacity
            ).retryWhen { cause, attempt ->
                delay(1000)
                attempt < 3
            }.catch {
                Log.e("GroupOpenViewModel", "create group error", it)
                _uiState.update { state ->
                    state.copy(
                        isLoading = false
                    )
                }
                _event.send(GroupOpenEvent.CreateGroupError(it))
            }.collectLatest {
                _event.send(GroupOpenEvent.CreateGroupSuccess(it))
            }
        }
    }
}