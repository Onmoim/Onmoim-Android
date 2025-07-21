package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.model.GroupDetail
import com.onmoim.core.data.repository.GroupRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
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

    private val _groupDetailUiState = MutableStateFlow<GroupDetailUiState>(GroupDetailUiState.Loading)
    val groupDetailUiState = _groupDetailUiState.asStateFlow()

    init {
        fetchGroupDetail()
    }

    fun fetchGroupDetail() {
        viewModelScope.launch {
            groupRepository.getGroupDetail(id).catch {
                _groupDetailUiState.value = GroupDetailUiState.Error(it)
            }.collectLatest {
                _groupDetailUiState.value = GroupDetailUiState.Success(it)
            }
        }
    }
}

sealed class GroupDetailUiState {
    data object Loading : GroupDetailUiState()
    data class Success(val groupDetail: GroupDetail) : GroupDetailUiState()
    data class Error(val t: Throwable) : GroupDetailUiState()
}