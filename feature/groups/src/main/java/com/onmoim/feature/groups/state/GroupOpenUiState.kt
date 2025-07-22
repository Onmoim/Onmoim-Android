package com.onmoim.feature.groups.state

data class GroupOpenUiState(
    val locationName: String = "",
    val locationId: Int = 0,
    val groupName: String = "",
    val groupDescription: String = "",
    val groupCapacity: Int? = null,
    val isLoading: Boolean = false
) {
    fun isValid() = locationName.isNotBlank() &&
            locationId > 0 &&
            groupName.isNotBlank() &&
            groupDescription.isNotBlank() &&
            groupCapacity != null &&
            groupCapacity in 5..300
}