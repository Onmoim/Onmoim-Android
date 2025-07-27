package com.onmoim.feature.profile

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.onmoim.feature.groups.navigation.navigateToGroupDetail
import com.onmoim.feature.profile.constant.GroupType
import com.onmoim.feature.profile.view.GroupListRoute
import com.onmoim.feature.profile.view.NotificationSettingRoute
import com.onmoim.feature.profile.view.ProfileEditRoute
import com.onmoim.feature.profile.view.ProfileRoute
import com.onmoim.feature.profile.viewmodel.GroupListViewModel
import kotlinx.serialization.Serializable

@Serializable
object ProfileNavigation

@Serializable
object ProfileRoute

@Serializable
object ProfileEditRoute

@Serializable
data class GroupListRoute(
    val groupType: GroupType
)

@Serializable
object NotificationSettingRoute

fun NavController.navigateToProfileEdit(navOptions: NavOptions? = null) {
    navigate(ProfileEditRoute, navOptions)
}

fun NavController.navigateToGroupList(groupType: GroupType, navOptions: NavOptions? = null) {
    navigate(GroupListRoute(groupType), navOptions)
}

fun NavController.navigateToNotificationSetting(navOptions: NavOptions? = null) {
    navigate(NotificationSettingRoute, navOptions)
}

fun NavGraphBuilder.profileGraph(
    navController: NavController,
    bottomBar: @Composable () -> Unit
) {
    navigation<ProfileNavigation>(
        startDestination = ProfileRoute
    ) {
        composable<ProfileRoute>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            ProfileRoute(
                bottomBar = bottomBar,
                onNavigateToProfileEdit = {
                    navController.navigateToProfileEdit()
                },
                onNavigateToGroupList = {
                    navController.navigateToGroupList(it)
                },
                onNavigateToNotificationSetting = {
                    navController.navigateToNotificationSetting()
                }
            )
        }
        composable<ProfileEditRoute> {
            ProfileEditRoute()
        }
        composable<GroupListRoute> { backStackEntry ->
            val groupType = backStackEntry.toRoute<GroupListRoute>().groupType
            val groupListViewModel = hiltViewModel<GroupListViewModel, GroupListViewModel.Factory> {
                it.create(groupType)
            }

            GroupListRoute(
                groupListViewModel = groupListViewModel,
                onNavigateToGroupDetail = {
                    navController.navigateToGroupDetail(it)
                }
            )
        }
        composable<NotificationSettingRoute> {
            NotificationSettingRoute()
        }
    }
}