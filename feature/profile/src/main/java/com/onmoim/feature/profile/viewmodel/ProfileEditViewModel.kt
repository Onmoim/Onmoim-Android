package com.onmoim.feature.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.repository.UserRepository
import com.onmoim.core.designsystem.constant.Gender
import com.onmoim.feature.profile.state.ProfileEditEvent
import com.onmoim.feature.profile.state.ProfileEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState = _uiState.asStateFlow()

    private val _isLoadingState = MutableStateFlow(false)
    val isLoadingState = _isLoadingState.asStateFlow()

    private val _event = Channel<ProfileEditEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        _isLoadingState.value = true

        viewModelScope.launch {
            userRepository.getMyProfile().catch {
                Log.e("ProfileEditViewModel", "fetchProfile error", it)
                _isLoadingState.value = false
            }.collectLatest {
                _uiState.update { state ->
                    state.copy(
                        id = it.id,
                        name = it.name,
                        gender = when (it.gender) {
                            "M" -> Gender.MALE
                            "F" -> Gender.FEMALE
                            else -> null
                        },
                        birth = it.birth,
                        locationId = it.locationId,
                        locationName = it.location,
                        introduction = it.introduction ?: "",
                        categoryIds = it.interestCategoryIds,
                        categoryNames = it.interestCategories,
                        originImageUrl = it.profileImgUrl
                    )
                }
                _isLoadingState.value = false
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(name = value)
        }
    }

    fun onGenderChange(value: Gender) {
        _uiState.update {
            it.copy(gender = value)
        }
    }

    fun onBirthChange(value: LocalDate) {
        _uiState.update {
            it.copy(birth = value)
        }
    }

    fun onLocationChange(id: Int, name: String) {
        _uiState.update {
            it.copy(locationId = id, locationName = name)
        }
    }

    fun onIntroductionChange(value: String) {
        _uiState.update {
            it.copy(introduction = value)
        }
    }

    fun onProfileImageChange(value: String) {
        _uiState.update {
            it.copy(newImagePath = value)
        }
    }

    fun updateProfile() {
        val id = _uiState.value.id ?: return
        val name = _uiState.value.name
        val gender = _uiState.value.gender ?: return
        val birth = _uiState.value.birth ?: return
        val locationId = _uiState.value.locationId ?: return
        val introduction = _uiState.value.introduction
        val categoryIds = _uiState.value.categoryIds
        val originImageUrl = _uiState.value.originImageUrl
        val newImagePath = _uiState.value.newImagePath

        viewModelScope.launch {
            _isLoadingState.value = true

            userRepository.updateProfile(
                id = id,
                name = name,
                gender = when (gender) {
                    Gender.MALE -> "M"
                    Gender.FEMALE -> "F"
                },
                birth = birth.toString(),
                locationId = locationId,
                introduction = introduction,
                categoryIds = categoryIds,
                originImageUrl = originImageUrl,
                imagePath = newImagePath,
            ).onFailure {
                Log.e("ProfileEditViewModel", "updateProfile error", it)
                _isLoadingState.value = false
                _event.send(ProfileEditEvent.UpdateFailure(it))
            }.onSuccess {
                _isLoadingState.value = false
                _event.send(ProfileEditEvent.UpdateSuccess)
            }
        }
    }
}