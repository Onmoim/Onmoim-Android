package com.onmoim.core.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.onmoim.feature.login.navigation.LoginNavigation
import com.onmoim.feature.login.navigation.loginGraph

private const val PAGE_TURN_DURATION_MS = 450

@Composable
fun OnmoimNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = LoginNavigation, // FIXME: 추후 수정 필요
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
        loginGraph(
            navController = navController
        )
    }
}