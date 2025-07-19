package com.onmoim.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.onmoim.feature.category.CategoryRoute
import com.onmoim.feature.category.view.CategoryRoute
import com.onmoim.feature.groups.groupsGraph
import com.onmoim.feature.groups.navigateToGroupDetail
import com.onmoim.feature.home.homeGraph
import com.onmoim.feature.login.navigation.LoginNavigation
import com.onmoim.feature.login.navigation.loginGraph
import com.onmoim.feature.profile.profileGraph

private const val PAGE_TURN_DURATION_MS = 450

@Composable
fun OnmoimNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = LoginNavigation,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(PAGE_TURN_DURATION_MS)
            ) + fadeIn(animationSpec = tween(PAGE_TURN_DURATION_MS))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = PAGE_TURN_DURATION_MS),
                targetOffset = { it / 2 }
            ) + fadeOut(animationSpec = tween(PAGE_TURN_DURATION_MS))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = PAGE_TURN_DURATION_MS),
                initialOffset = { it / 2 }
            ) + fadeIn(animationSpec = tween(PAGE_TURN_DURATION_MS))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(PAGE_TURN_DURATION_MS)
            ) + fadeOut(animationSpec = tween(PAGE_TURN_DURATION_MS))
        }
    ) {
        homeGraph(
            navController = navController,
            topBar = topBar,
            bottomBar = bottomBar
        )
        composable<CategoryRoute>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            CategoryRoute(
                topBar = topBar,
                bottomBar = bottomBar,
                onNavigateToGroupDetail = {
                    navController.navigateToGroupDetail(it)
                }
            )
        }
        groupsGraph(
            navController = navController,
            topBar = topBar,
            bottomBar = bottomBar
        )
        profileGraph(
            navController = navController,
            bottomBar = bottomBar
        )
        loginGraph(
            navController = navController
        )
    }
}