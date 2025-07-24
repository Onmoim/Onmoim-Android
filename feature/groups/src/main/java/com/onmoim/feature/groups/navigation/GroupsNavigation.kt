package com.onmoim.feature.groups.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.onmoim.feature.groups.constant.GroupMemberRole
import kotlinx.serialization.Serializable

@Serializable
object GroupsNavigation

@Serializable
object MyGroupRoute

@Serializable
data class GroupDetailRoute(
    val id: Int
)

@Serializable
data class ComingScheduleRoute(
    val groupId: Int? = null,
    val role: GroupMemberRole? = null
)

@Serializable
object GroupCategorySelectRoute

@Serializable
data class GroupOpenRoute(
    val categoryId: Int,
    val categoryName: String,
    val categoryImageUrl: String?
)

@Serializable
data class GroupOpenCompleteRoute(
    val groupId: Int
)

@Serializable
data class GroupManagementRoute(
    val groupId: Int
)

@Serializable
data class GroupEditRoute(
    val groupId: Int
)

@Serializable
data class ScheduleManagementRoute(
    val groupId: Int
)

@Serializable
data class CreateScheduleRoute(
    val groupId: Int,
    val groupMemberRole: GroupMemberRole
)

@Serializable
object MeetingPlaceSearchRoute

fun NavController.navigateToComingSchedule(
    groupId: Int? = null,
    groupMemberRole: GroupMemberRole? = null,
    navOptions: NavOptions? = null
) {
    navigate(ComingScheduleRoute(groupId, groupMemberRole), navOptions)
}

fun NavController.navigateToGroupDetail(id: Int, navOptions: NavOptions? = null) {
    navigate(GroupDetailRoute(id), navOptions)
}

fun NavController.navigateToGroupCategorySelect(navOptions: NavOptions? = null) {
    navigate(GroupCategorySelectRoute, navOptions)
}

fun NavController.navigateToGroupOpen(
    categoryId: Int,
    categoryName: String,
    categoryImageUrl: String?,
    navOptions: NavOptions? = null
) {
    navigate(GroupOpenRoute(categoryId, categoryName, categoryImageUrl), navOptions)
}

fun NavController.navigateToGroupOpenComplete(groupId: Int, navOptions: NavOptions? = null) {
    navigate(GroupOpenCompleteRoute(groupId), navOptions)
}

fun NavController.navigateToGroupManagement(groupId: Int, navOptions: NavOptions? = null) {
    navigate(GroupManagementRoute(groupId), navOptions)
}

fun NavController.navigateToGroupEdit(groupId: Int, navOptions: NavOptions? = null) {
    navigate(GroupEditRoute(groupId), navOptions)
}

fun NavController.navigateToScheduleManagement(groupId: Int, navOptions: NavOptions? = null) {
    navigate(ScheduleManagementRoute(groupId), navOptions)
}

fun NavController.navigateToCreateSchedule(
    groupId: Int,
    groupMemberRole: GroupMemberRole,
    navOptions: NavOptions? = null
) {
    navigate(CreateScheduleRoute(groupId, groupMemberRole), navOptions)
}

fun NavController.navigateToMeetingLocationSearch(navOptions: NavOptions? = null) {
    navigate(MeetingPlaceSearchRoute, navOptions)
}