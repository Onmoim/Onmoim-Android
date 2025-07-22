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
import com.onmoim.feature.groups.view.ComingScheduleRoute
import com.onmoim.feature.groups.view.GroupCategorySelectRoute
import com.onmoim.feature.groups.view.GroupOpenCompleteRoute
import com.onmoim.feature.groups.view.GroupOpenRoute
import com.onmoim.feature.groups.view.MyGroupRoute
import com.onmoim.feature.groups.view.groupdetail.GroupDetailRoute
import com.onmoim.feature.groups.view.groupmanagement.GroupManagementRoute
import com.onmoim.feature.groups.viewmodel.GroupDetailViewModel
import com.onmoim.feature.groups.viewmodel.GroupManagementViewModel
import com.onmoim.feature.groups.viewmodel.GroupOpenViewModel
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
    val id: Int? = null
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

fun NavController.navigateToComingSchedule(groupId: Int? = null, navOptions: NavOptions? = null) {
    navigate(ComingScheduleRoute(groupId), navOptions)
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
            val id = backStackEntry.toRoute<ComingScheduleRoute>().id

            ComingScheduleRoute()
        }
        composable<GroupDetailRoute> { backStackEntry ->
            val groupId = backStackEntry.toRoute<GroupDetailRoute>().id
            val groupDetailViewModel =
                hiltViewModel<GroupDetailViewModel, GroupDetailViewModel.Factory> {
                    it.create(groupId)
                }

            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val isRefresh =
                savedStateHandle?.get<Boolean>(GroupsNavigationBundleKey.REFRESH) ?: false
            savedStateHandle?.remove<Boolean>(GroupsNavigationBundleKey.REFRESH)

            GroupDetailRoute(
                groupDetailViewModel = groupDetailViewModel,
                onNavigateToComingSchedule = {
                    navController.navigateToComingSchedule(groupId)
                },
                onNavigateToPostDetail = {},
                onNavigateToGroupManagement = {
                    navController.navigateToGroupManagement(it)
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
                        set(GroupsNavigationBundleKey.REFRESH, true)
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}