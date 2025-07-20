package com.onmoim.feature.login.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.onmoim.feature.home.navigateToHome
import com.onmoim.feature.location.navigation.LocationNavigationBundleKey
import com.onmoim.feature.location.navigation.navigateToLocationSearch
import com.onmoim.feature.login.view.InterestSelectRoute
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
object InterestSelectRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(LoginRoute, navOptions)
}

fun NavController.navigateToProfileSetting(navOptions: NavOptions? = null) {
    navigate(ProfileSettingRoute, navOptions)
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
                    navController.navigateToHome(
                        navOptions {
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    )
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
            val locationName = savedStateHandle?.get<String>(LocationNavigationBundleKey.LOCATION_NAME) ?: ""
            val locationId = savedStateHandle?.get<Int>(LocationNavigationBundleKey.LOCATION_ID) ?: 0
            savedStateHandle?.remove<String>(LocationNavigationBundleKey.LOCATION_NAME)
            savedStateHandle?.remove<Int>(LocationNavigationBundleKey.LOCATION_ID)

            val profileSettingViewModel = hiltViewModel<ProfileSettingViewModel>()

            ProfileSettingRoute(
                profileSettingViewModel = profileSettingViewModel,
                onNavigateToLocationSetting = {
                    navController.navigateToLocationSearch()
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
                profileSettingViewModel.onLocationChange(locationName, locationId)
            }
        }
        composable<InterestSelectRoute> {
            InterestSelectRoute(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigateToHome(
                        navOptions {
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    )
                }
            )
        }
    }
}