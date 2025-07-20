package com.onmoim.feature.location.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.onmoim.feature.location.LocationSearchRoute
import kotlinx.serialization.Serializable

@Serializable
object LocationSearchRoute

fun NavController.navigateToLocationSearch(navOptions: NavOptions? = null) {
    navigate(LocationSearchRoute, navOptions)
}

fun NavGraphBuilder.locationGraph(
    navController: NavController
) {
    composable<LocationSearchRoute> {
        LocationSearchRoute(
            onBack = { address, addressId ->
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set(LocationNavigationBundleKey.LOCATION_NAME, address)
                    set(LocationNavigationBundleKey.LOCATION_ID, addressId)
                }
                navController.popBackStack()
            }
        )
    }
}