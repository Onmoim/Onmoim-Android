package com.onmoim.feature.groups.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.onmoim.feature.groups.constant.GroupMemberRole
import com.onmoim.feature.groups.view.ComingScheduleRoute
import com.onmoim.feature.groups.view.GroupCategorySelectRoute
import com.onmoim.feature.groups.view.GroupEditRoute
import com.onmoim.feature.groups.view.GroupOpenCompleteRoute
import com.onmoim.feature.groups.view.GroupOpenRoute
import com.onmoim.feature.groups.view.MyGroupRoute
import com.onmoim.feature.groups.view.ScheduleManagementRoute
import com.onmoim.feature.groups.view.groupdetail.GroupDetailRoute
import com.onmoim.feature.groups.view.groupmanagement.GroupManagementRoute
import com.onmoim.feature.groups.viewmodel.ComingScheduleViewModel
import com.onmoim.feature.groups.viewmodel.GroupDetailViewModel
import com.onmoim.feature.groups.viewmodel.GroupEditViewModel
import com.onmoim.feature.groups.viewmodel.GroupManagementViewModel
import com.onmoim.feature.groups.viewmodel.GroupOpenViewModel
import com.onmoim.feature.groups.viewmodel.ScheduleManagementViewModel
import com.onmoim.feature.location.navigation.LocationNavigationBundleKey
import com.onmoim.feature.location.navigation.navigateToLocationSearch
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

fun NavGraphBuilder.groupsGraph(
    navController: NavController,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    navigation<GroupsNavigation>(
        startDestination = MyGroupRoute
    ) {
        composable<MyGroupRoute>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            MyGroupRoute(
                topBar = topBar,
                bottomBar = bottomBar,
                onNavigateToGroupCategorySelect = {
                    navController.navigateToGroupCategorySelect()
                },
                onNavigateToComingSchedule = {
                    navController.navigateToComingSchedule()
                }
            )
        }
        composable<ComingScheduleRoute> { backStackEntry ->
            val comingScheduleRoute = backStackEntry.toRoute<ComingScheduleRoute>()
            val comingScheduleViewModel =
                hiltViewModel<ComingScheduleViewModel, ComingScheduleViewModel.Factory> {
                    it.create(comingScheduleRoute.groupId)
                }

            ComingScheduleRoute(
                comingScheduleViewModel = comingScheduleViewModel,
                onNavigateToCreateSchedule = {

                },
                onNavigateToMeetingLocation = {

                }
            )
        }
        composable<GroupDetailRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<GroupDetailRoute>().id
            val groupDetailViewModel =
                hiltViewModel<GroupDetailViewModel, GroupDetailViewModel.Factory> {
                    it.create(groupId)
                }

            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val isRefresh =
                savedStateHandle?.get<Boolean>(GroupsNavigationBundleKey.GROUP_DETAIL_REFRESH)
                    ?: false
            savedStateHandle?.remove<Boolean>(GroupsNavigationBundleKey.GROUP_DETAIL_REFRESH)

            GroupDetailRoute(
                groupDetailViewModel = groupDetailViewModel,
                onNavigateToComingSchedule = { role ->
                    navController.navigateToComingSchedule(groupId, role)
                },
                onNavigateToPostDetail = {},
                onNavigateToGroupManagement = {
                    navController.navigateToGroupManagement(groupId)
                }
            )

            LaunchedEffect(Unit) {
                if (isRefresh) {
                    groupDetailViewModel.fetchGroupDetail(true)
                }
            }
        }
        composable<GroupCategorySelectRoute> {
            GroupCategorySelectRoute(
                onNavigateToGroupOpen = { categoryId, categoryName, categoryImageUrl ->
                    navController.navigateToGroupOpen(categoryId, categoryName, categoryImageUrl)
                }
            )
        }
        composable<GroupOpenRoute> { backStackEntry ->
            val groupOpenRoute = backStackEntry.toRoute<GroupOpenRoute>()
            val groupOpenViewModel = hiltViewModel<GroupOpenViewModel, GroupOpenViewModel.Factory> {
                it.create(groupOpenRoute.categoryId)
            }

            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val locationId =
                savedStateHandle?.get<Int>(LocationNavigationBundleKey.LOCATION_ID) ?: 0
            val locationName =
                savedStateHandle?.get<String>(LocationNavigationBundleKey.LOCATION_NAME) ?: ""
            savedStateHandle?.remove<String>(LocationNavigationBundleKey.LOCATION_NAME)
            savedStateHandle?.remove<Int>(LocationNavigationBundleKey.LOCATION_ID)

            GroupOpenRoute(
                groupOpenViewModel = groupOpenViewModel,
                categoryName = groupOpenRoute.categoryName,
                categoryImageUrl = groupOpenRoute.categoryImageUrl,
                onNavigateToLocationSearch = {
                    navController.navigateToLocationSearch()
                },
                onNavigateToGroupOpenComplete = {
                    navController.navigateToGroupOpenComplete(it, navOptions {
                        popUpTo(MyGroupRoute)
                    })
                }
            )

            LaunchedEffect(Unit) {
                groupOpenViewModel.onLocationChange(locationId, locationName)
            }
        }
        composable<GroupOpenCompleteRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<GroupOpenCompleteRoute>().groupId

            GroupOpenCompleteRoute(
                onNavigateToGroupDetail = {
                    navController.navigateToGroupDetail(groupId, navOptions {
                        popUpTo(MyGroupRoute)
                    })
                }
            )
        }
        composable<GroupManagementRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<GroupManagementRoute>().groupId
            val groupManagementViewModel =
                hiltViewModel<GroupManagementViewModel, GroupManagementViewModel.Factory> {
                    it.create(groupId)
                }

            GroupManagementRoute(
                groupManagementViewModel = groupManagementViewModel,
                onBackAndRefresh = {
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(GroupsNavigationBundleKey.GROUP_DETAIL_REFRESH, true)
                    }
                    navController.popBackStack()
                },
                onNavigateToGroupEdit = {
                    navController.navigateToGroupEdit(groupId)
                },
                onNavigateToScheduleManagement = {
                    navController.navigateToScheduleManagement(groupId)
                }
            )
        }
        composable<GroupEditRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<GroupEditRoute>().groupId
            val groupEditViewModel = hiltViewModel<GroupEditViewModel, GroupEditViewModel.Factory> {
                it.create(groupId)
            }

            GroupEditRoute(
                groupEditViewModel = groupEditViewModel,
                onBackAndRefresh = {
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(GroupsNavigationBundleKey.GROUP_DETAIL_REFRESH, true)
                    }
                    navController.popBackStack()
                }
            )
        }
        composable<ScheduleManagementRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<ScheduleManagementRoute>().groupId
            val scheduleManagementViewModel =
                hiltViewModel<ScheduleManagementViewModel, ScheduleManagementViewModel.Factory> {
                    it.create(groupId)
                }

            ScheduleManagementRoute(
                scheduleManagementViewModel = scheduleManagementViewModel
            )
        }
    }
}