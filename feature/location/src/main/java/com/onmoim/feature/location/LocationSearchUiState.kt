package com.onmoim.feature.location

import com.onmoim.core.data.model.Location

sealed class LocationSearchUiState {
    data class Result(val locations: List<Location>) : LocationSearchUiState()
    data class Error(val t: Throwable) : LocationSearchUiState()
}