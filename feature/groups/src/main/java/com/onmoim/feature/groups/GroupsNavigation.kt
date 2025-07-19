package com.onmoim.feature.groups

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
import com.onmoim.feature.groups.view.GroupDetailRoute
import com.onmoim.feature.groups.view.MyGroupRoute
import com.onmoim.feature.groups.viewmodel.GroupDetailViewModel
import kotlinx.serialization.Serializable

@Serializable
object GroupsNavigation

@Serializable
object MyGroupRoute

@Serializable
data class GroupDetailRoute(
    val id: Int
)

fun NavController.navigateToGroupDetail(id: Int, navOptions: NavOptions? = null) {
    navigate(GroupDetailRoute(id), navOptions)
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
                bottomBar = bottomBar
            )
        }
        composable<GroupDetailRoute> { backStackEntry ->
            val id = backStackEntry.toRoute<GroupDetailRoute>().id
            val groupDetailViewModel =
                hiltViewModel<GroupDetailViewModel, GroupDetailViewModel.Factory> {
                    it.create(id)
                }

            GroupDetailRoute(
                groupDetailViewModel = groupDetailViewModel,
                onNavigateToComingSchedule = {},
                onNavigateToPostDetail = {}
            )
        }
    }
}