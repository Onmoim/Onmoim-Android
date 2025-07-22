package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.feature.groups.state.GroupEditUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GroupEditViewModel.Factory::class)
class GroupEditViewModel @AssistedInject constructor(
    @Assisted("groupId") private val groupId: Int,
    private val groupRepository: GroupRepository
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("groupId") groupId: Int
        ): GroupEditViewModel
    }

    private val _uiState = MutableStateFlow(GroupEditUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchGroupDetail()
    }

    fun fetchGroupDetail() {
        viewModelScope.launch {
            groupRepository.getGroupDetail(groupId).retryWhen { cause, attempt ->
                delay(1000)
                attempt < 3
            }.catch {
                Log.e("GroupEditViewModel", "fetchGroupDetail error", it)
            }.collectLatest {
                _uiState.update { state ->
                    state.copy(
                        locationName = it.location,
                        groupName = it.title,
                        groupDescription = it.description,
                        groupCapacity = null, // TODO: 추후 수정
                        groupImageUrl = it.imageUrl,
                        categoryName = it.category,
                        categoryImageUrl = null, // TODO: 추후 수정
                    )
                }
            }
        }
    }

    fun onImageChange(imageUrl: String) {
        _uiState.value = _uiState.value.copy(
            newGroupImageUrl = imageUrl
        )
    }

    fun onGroupDescriptionChange(description: String) {
        _uiState.value = _uiState.value.copy(
            newGroupDescription = description
        )
    }

    fun onGroupCapacityChange(capacity: Int?) {
        _uiState.value = _uiState.value.copy(
            newGroupCapacity = capacity
        )
    }

    fun updateGroupInfo() {

    }
}