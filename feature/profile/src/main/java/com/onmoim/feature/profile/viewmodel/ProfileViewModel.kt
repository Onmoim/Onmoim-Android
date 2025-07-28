package com.onmoim.feature.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.model.Profile
import com.onmoim.core.data.repository.UserRepository
import com.onmoim.core.domain.usecase.GetUserIdUseCase
import com.onmoim.core.event.AuthEventBus
import com.onmoim.feature.profile.state.ProfileEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val authEventBus: AuthEventBus
) : ViewModel() {
    private val _event = Channel<ProfileEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _profileState = MutableStateFlow<Profile?>(null)
    val profileState = _profileState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile(refresh: Boolean = false) {
        if (refresh) {
            _isLoading.value = true
        }

        viewModelScope.launch {
            userRepository.getMyProfile().retryWhen { cause, attempt ->
                Log.e("ProfileViewModel", "fetchProfile error", cause)
                attempt < 3
            }.catch {
                Log.e("ProfileViewModel", "fetchProfile error", it)
                if (refresh) {
                    _isLoading.value = false
                }
            }.collectLatest {
                if (refresh) {
                    _isLoading.value = false
                }
                _profileState.value = it
            }
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = getUserIdUseCase()
            userRepository.withdrawal(userId).onFailure {
                Log.e("ProfileViewModel", "withdrawal error", it)
                _isLoading.value = false
                _event.send(ProfileEvent.WithdrawalError(it))
            }.onSuccess {
                _isLoading.value = false
                authEventBus.notifyWithdrawal()
            }
        }
    }
}