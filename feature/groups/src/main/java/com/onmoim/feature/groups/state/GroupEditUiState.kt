package com.onmoim.feature.groups.state

data class GroupEditUiState(
    val locationName: String = "",
    val groupName: String = "",
    val groupDescription: String = "",
    val newGroupDescription: String = groupDescription,
    val groupCapacity: Int? = null,
    val newGroupCapacity: Int? = groupCapacity,
    val groupImageUrl: String? = null,
    val newGroupImageUrl: String? = groupImageUrl,
    val categoryName: String = "",
    val categoryImageUrl: String? = null,
    val isLoading: Boolean = false,
) {
    fun isValid() =
        (groupDescription != newGroupDescription || groupCapacity != newGroupCapacity || newGroupImageUrl != null) && newGroupDescription.isNotBlank() && newGroupCapacity != null
}