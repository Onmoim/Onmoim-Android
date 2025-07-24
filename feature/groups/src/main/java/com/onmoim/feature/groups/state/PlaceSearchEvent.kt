package com.onmoim.feature.groups.state

sealed class PlaceSearchEvent {
    data object SearchEmpty: PlaceSearchEvent()
    data class SearchFailure(val t: Throwable): PlaceSearchEvent()
}