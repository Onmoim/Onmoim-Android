package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.feature.groups.constant.MyGroupTab
import com.onmoim.feature.groups.state.JoinedGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
): ViewModel() {
    private val _selectedTabState = MutableStateFlow(MyGroupTab.MY_GROUP)
    val selectedTabState = _selectedTabState.asStateFlow()

    private val _joinedGroupUiState = MutableStateFlow<JoinedGroupUiState>(JoinedGroupUiState.Loading)
    val joinedGroupUiState = _joinedGroupUiState.asStateFlow()

    init {
        fetchJoinedGroup()
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
}