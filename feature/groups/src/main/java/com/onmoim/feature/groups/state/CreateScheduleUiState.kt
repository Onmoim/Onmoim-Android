package com.onmoim.feature.groups.state

import com.onmoim.feature.groups.constant.GroupMemberRole
import com.onmoim.feature.groups.constant.ScheduleType
import java.time.LocalDate
import java.time.LocalTime

data class CreateScheduleUiState(
    val imagePath: String? = null,
    val type: ScheduleType? = null,
    val name: String = "",
    val startDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val place: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val cost: Long? = null,
    val capacity: Int? = null,
    val isLoading: Boolean = false
) {
    fun isValid(role: GroupMemberRole) = name.isNotBlank() &&
            startDate != null &&
            startTime != null &&
            place.isNotBlank() &&
            latitude != null &&
            longitude != null &&
            cost != null &&
            cost in 0..Long.MAX_VALUE &&
            capacity != null &&
            capacity in 2..Int.MAX_VALUE &&
            (role == GroupMemberRole.MEMBER || (role == GroupMemberRole.OWNER && type != null))
}