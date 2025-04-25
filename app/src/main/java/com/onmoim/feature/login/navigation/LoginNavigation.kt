package com.onmoim.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object LoginNavigation

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
                onNavigateToSignUp = {

                }
            )
        }
    }
}