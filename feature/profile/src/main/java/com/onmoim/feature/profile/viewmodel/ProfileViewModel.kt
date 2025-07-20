package com.onmoim.feature.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.model.Profile
import com.onmoim.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _profileState = MutableStateFlow<Profile?>(null)
    val profileState = _profileState.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            userRepository.getMyProfile().retryWhen { cause, attempt ->
                Log.e("ProfileViewModel", "fetchProfile error", cause)
                attempt < 3
            }.collectLatest {
                _profileState.value = it
            }
        }
    }
}