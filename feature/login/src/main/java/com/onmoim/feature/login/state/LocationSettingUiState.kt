package com.onmoim.feature.login.state

import com.onmoim.core.data.model.Location

sealed class LocationSettingUiState {
    data class Result(val locations: List<Location>) : LocationSettingUiState()
    data class Error(val t: Throwable) : LocationSettingUiState()
}