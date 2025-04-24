package com.onmoim.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.onmoim.feature.login.view.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
object LoginNavigation

@Serializable
object LoginRoute

fun NavGraphBuilder.loginGraph(
    navController: NavController
) {
    navigation<LoginNavigation>(
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen()
        }
    }
}