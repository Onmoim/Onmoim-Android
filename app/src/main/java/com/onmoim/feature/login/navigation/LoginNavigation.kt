package com.onmoim.feature.login.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.onmoim.feature.login.view.InterestSelectRoute
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
object ProfileSettingRoute

@Serializable
object LocationSettingRoute

@Serializable
object InterestSelectRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(LoginRoute, navOptions)
}

fun NavController.navigateToProfileSetting(navOptions: NavOptions? = null) {
    navigate(ProfileSettingRoute, navOptions)
}

fun NavController.navigateToLocationSetting(navOptions: NavOptions? = null) {
    navigate(LocationSettingRoute, navOptions)
}

fun NavController.navigateToInterestSelect(navOptions: NavOptions? = null) {
    navigate(InterestSelectRoute, navOptions)
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
                },
                onNavigateToInterestSelect = {
                    navController.navigateToInterestSelect()
                }
            )
        }
        composable<ProfileSettingRoute> {
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val address = savedStateHandle?.get<String>(LoginNavigationBundleKey.ADDRESS) ?: ""
            val addressId = savedStateHandle?.get<Int>(LoginNavigationBundleKey.ADDRESS_ID) ?: 0
            savedStateHandle?.remove<String>(LoginNavigationBundleKey.ADDRESS)
            savedStateHandle?.remove<Int>(LoginNavigationBundleKey.ADDRESS_ID)

            val profileSettingViewModel = hiltViewModel<ProfileSettingViewModel>()

            ProfileSettingRoute(
                profileSettingViewModel = profileSettingViewModel,
                onNavigateToLocationSetting = {
                    navController.navigateToLocationSetting()
                },
                onNavigateToInterestSelect = {
                    navController.navigateToInterestSelect(navOptions {
                        popUpTo(LoginRoute) {
                            inclusive = true
                        }
                    })
                }
            )

            LaunchedEffect(Unit) {
                profileSettingViewModel.onAddressChange(address, addressId)
            }
        }
        composable<LocationSettingRoute> {
            LocationSettingRoute(
                onBack = { address, addressId ->
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(LoginNavigationBundleKey.ADDRESS, address)
                        set(LoginNavigationBundleKey.ADDRESS_ID, addressId)
                    }
                    navController.popBackStack()
                }
            )
        }
        composable<InterestSelectRoute> {
            InterestSelectRoute(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {

                }
            )
        }
    }
}