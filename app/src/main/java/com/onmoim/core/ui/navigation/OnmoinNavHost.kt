package com.onmoim.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.onmoim.feature.login.LoginNavigation
import com.onmoim.feature.login.loginGraph

@Composable
fun OnmoimNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = LoginNavigation // TODO: 추후 수정 필요
    ) {
        loginGraph(
            navController = navController
        )
    }
}