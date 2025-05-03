package com.onmoim.feature.login.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.onmoim.feature.login.view.LocationSettingRoute
import com.onmoim.feature.login.view.LoginRoute
import com.onmoim.feature.login.view.ProfileSettingRoute
import com.onmoim.feature.login.viewmodel.ProfileSettingViewModel
import kotlinx.serialization.Serializable

@Serializable
object LoginNavigation

@Serializable
object LoginRoute

@Serializable
data object ProfileSettingRoute

@Serializable
data object LocationSettingRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(LoginRoute, navOptions)
}

fun NavController.navigateToProfileSetting(navOptions: NavOptions? = null) {
    navigate(ProfileSettingRoute, navOptions)
}

fun NavController.navigateToLocationSetting(navOptions: NavOptions? = null) {
    navigate(LocationSettingRoute, navOptions)
}

fun NavGraphBuilder.loginGraph(
    navController: NavController
) {
    navigation<LoginNavigation>(
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginRoute(
                loginViewModel = hiltViewModel(),
                onNavigateToHome = {

                },
                onNavigateToProfileSetting = {
                    navController.navigateToProfileSetting()
                }
            )
        }
        composable<ProfileSettingRoute> {
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val location = savedStateHandle?.get<String>(LoginNavigationBundleKey.LOCATION)
            savedStateHandle?.remove<String>(LoginNavigationBundleKey.LOCATION)

            val profileSettingViewModel = hiltViewModel<ProfileSettingViewModel>()

            ProfileSettingRoute(
                profileSettingViewModel = profileSettingViewModel,
                onNavigateToLocationSetting = {
                    navController.navigateToLocationSetting()
                },
                onNavigateToSelectInterest = {

                }
            )

            LaunchedEffect(Unit) {
                profileSettingViewModel.onLocationChange(location ?: "")
            }
        }
        composable<LocationSettingRoute> {
            LocationSettingRoute(
                locationSettingViewModel = hiltViewModel(),
                onBack = { address ->
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(LoginNavigationBundleKey.LOCATION, address)
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}