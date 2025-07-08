package com.onmoim.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeRoute, navOptions)
}