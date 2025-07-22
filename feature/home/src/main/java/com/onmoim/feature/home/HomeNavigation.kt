package com.onmoim.feature.home

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
import com.onmoim.feature.home.constant.HomeGroupType
import com.onmoim.feature.home.view.GroupMoreRoute
import com.onmoim.feature.home.view.HomeRoute
import com.onmoim.feature.home.viewmodel.GroupMoreViewModel
import kotlinx.serialization.Serializable

@Serializable
object HomeNavigation

@Serializable
object HomeRoute

@Serializable
data class GroupMoreRoute(
    val homeGroupType: HomeGroupType
)

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeRoute, navOptions)
}

fun NavController.navigateToGroupMore(
    homeGroupType: HomeGroupType,
    navOptions: NavOptions? = null
) {
    navigate(
        GroupMoreRoute(homeGroupType = homeGroupType),
        navOptions
    )
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    navigation<HomeNavigation>(
        startDestination = HomeRoute
    ) {
        composable<HomeRoute>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            HomeRoute(
                topBar = topBar,
                bottomBar = bottomBar,
                onNavigateToGroupDetail = {
                    navController.navigateToGroupDetail(it)
                },
                onNavigateToMoreGroup = {
                    navController.navigateToGroupMore(it)
                }
            )
        }
        composable<GroupMoreRoute> { backStackEntry ->
            val homeGroupType = backStackEntry.toRoute<GroupMoreRoute>().homeGroupType
            val groupMoreViewModel = hiltViewModel<GroupMoreViewModel, GroupMoreViewModel.Factory> {
                it.create(homeGroupType)
            }

            GroupMoreRoute(
                groupMoreViewModel = groupMoreViewModel,
                onNavigateToGroupDetail = {
                    navController.navigateToGroupDetail(it)
                }
            )
        }
    }
}