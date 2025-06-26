package com.onmoim.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.InterestRepository
import com.onmoim.core.data.repository.UserRepository
import com.onmoim.domain.usecase.GetUserIdUseCase
import com.onmoim.feature.login.state.InterestSelectEvent
import com.onmoim.feature.login.state.InterestSelectUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestSelectViewModel @Inject constructor(
    private val interestRepository: InterestRepository,
    private val userRepository: UserRepository,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val appSettingRepository: AppSettingRepository
) : ViewModel() {
    private val _event = Channel<InterestSelectEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _selectedInterestIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedInterestIds = _selectedInterestIds.asStateFlow()

    private val _uiState = MutableStateFlow<InterestSelectUiState>(InterestSelectUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchInterests()
    }

    fun onClickCategory(id: Int) {
        val copySet = _selectedInterestIds.value.toMutableSet()
        if (copySet.contains(id)) {
            copySet.remove(id)
        } else {
            copySet.add(id)
        }
        _selectedInterestIds.value = copySet
    }

    fun onClickConfirm() {
        viewModelScope.launch {
            _event.send(InterestSelectEvent.Loading)

            try {
                val interestIds = selectedInterestIds.value.toList()
                val userId = getUserIdUseCase()

                userRepository.setInterest(userId, interestIds).onFailure {
                    _event.send(InterestSelectEvent.ShowErrorDialog(it))
                }.onSuccess {
                    appSettingRepository.setHasNotInterest(false)
                    _event.send(InterestSelectEvent.NavigateToHome)
                }
            } catch (e: Exception) {
                _event.send(InterestSelectEvent.ShowErrorDialog(e))
            }
        }
    }

    fun fetchInterests() {
        viewModelScope.launch {
            _uiState.value = InterestSelectUiState.Loading

            interestRepository.getInterests().catch {
                _uiState.value = InterestSelectUiState.Error(it)
            }.collectLatest {
                _uiState.value = InterestSelectUiState.Success(it)
            }
        }
    }
}