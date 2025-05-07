package com.onmoim.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.constant.Gender
import com.onmoim.feature.login.state.ProfileSettingEvent
import com.onmoim.feature.login.state.ProfileSettingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(

) : ViewModel() {
    private val _profileSettingState = MutableStateFlow(ProfileSettingState())
    val profileSettingState = _profileSettingState.asStateFlow()

    private val _eventChannel = Channel<ProfileSettingEvent>(Channel.BUFFERED)
    val receiveEvent = _eventChannel.receiveAsFlow()

    fun onNameChange(name: String) {
        _profileSettingState.update {
            it.copy(name = name)
        }
    }

    fun onGenderChange(gender: Gender) {
        _profileSettingState.update {
            it.copy(gender = gender)
        }
    }

    fun onBirthChange(birth: String) {
        if (birth.length <= 8) {
            _profileSettingState.update {
                it.copy(birth = birth)
            }
        }
    }

    fun onLocationChange(location: String) {
        _profileSettingState.update {
            it.copy(location = location)
        }
    }

    fun onClickComplete() {
        viewModelScope.launch {
            _eventChannel.send(ProfileSettingEvent.Loading)
            // TODO: API 호출
        }
    }
}