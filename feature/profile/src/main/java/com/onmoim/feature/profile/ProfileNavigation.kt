package com.onmoim.feature.profile

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.onmoim.feature.profile.view.ProfileEditRoute
import com.onmoim.feature.profile.view.ProfileRoute
import kotlinx.serialization.Serializable

@Serializable
object ProfileNavigation

@Serializable
object ProfileRoute

@Serializable
object ProfileEditRoute

fun NavController.navigateToProfileEdit(navOptions: NavOptions? = null) {
    navigate(ProfileEditRoute, navOptions)
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
                }
            )
        }
        composable<ProfileEditRoute> {
            ProfileEditRoute()
        }
    }
}