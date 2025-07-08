package com.onmoim.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.constant.Gender
import com.onmoim.core.domain.usecase.SignUpUseCase
import com.onmoim.feature.login.state.ProfileSettingEvent
import com.onmoim.feature.login.state.ProfileSettingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _profileSettingState = MutableStateFlow(ProfileSettingState())
    val profileSettingState = _profileSettingState.asStateFlow()

    private val _event = Channel<ProfileSettingEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

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

    fun onBirthChange(localDate: LocalDate) {
        _profileSettingState.update {
            it.copy(birth = localDate)
        }
    }

    fun onLocationChange(location: String, locationId: Int) {
        _profileSettingState.update {
            it.copy(location = location, locationId = locationId)
        }
    }

    fun onClickConfirm() {
        viewModelScope.launch {
            _event.send(ProfileSettingEvent.Loading)
            val state = profileSettingState.value
            val birth = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(state.birth)

            signUpUseCase(
                locationId = state.locationId,
                birth = birth,
                gender = when(state.gender!!) {
                    Gender.MALE -> "M"
                    Gender.FEMALE -> "F"
                },
                name = state.name
            ).onFailure {
                Timber.e(it, "회원가입 실패")
                _event.send(ProfileSettingEvent.ProfileSettingFailed(it))
            }.onSuccess {
                _event.send(ProfileSettingEvent.ProfileSettingSuccess)
            }
        }
    }
}