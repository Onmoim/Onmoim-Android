package com.onmoim.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.onmoim.feature.login.view.LoginRoute
import com.onmoim.feature.login.view.ProfileSettingRoute
import kotlinx.serialization.Serializable

@Serializable
object LoginNavigation

@Serializable
object LoginRoute

@Serializable
data object ProfileSettingRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(LoginRoute, navOptions)
}

fun NavController.navigateToProfileSetting(navOptions: NavOptions? = null) {
    navigate(ProfileSettingRoute, navOptions)
}

fun NavGraphBuilder.loginGraph(
    navController: NavController
) {
    navigation<LoginNavigation>(
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginRoute(
                onNavigateToHome = {

                },
                onNavigateToProfileSetting = {
                    navController.navigateToProfileSetting()
                }
            )
        }
        composable<ProfileSettingRoute> {
            ProfileSettingRoute(
                onNavigateToLocationSetting = {

                },
                onNavigateToSelectInterest = {

                }
            )
        }
    }
}