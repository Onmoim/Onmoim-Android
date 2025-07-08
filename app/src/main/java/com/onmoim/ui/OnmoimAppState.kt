package com.onmoim.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.event.AuthEvent
import com.onmoim.core.event.AuthEventBus
import com.onmoim.feature.home.navigateToHome
import com.onmoim.feature.login.navigation.navigateToLogin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberOnmoimAppState(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    tokenRepository: TokenRepository,
    appSettingRepository: AppSettingRepository,
    authEventBus: AuthEventBus
): OnmoimAppState {
    return remember(
        navController,
        coroutineScope
    ) {
        OnmoimAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            tokenRepository = tokenRepository,
            appSettingRepository = appSettingRepository,
            authEventBus = authEventBus
        )
    }
}

@Stable
class OnmoimAppState(
    internal val navController: NavHostController,
    internal val coroutineScope: CoroutineScope,
    internal val tokenRepository: TokenRepository,
    internal val appSettingRepository: AppSettingRepository,
    internal val authEventBus: AuthEventBus
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    var shouldShowSplash = true
        private set

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry =
                navController.currentBackStackEntryFlow.collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    init {
        handleAuthEvent()
        checkAuthentication()
    }

    private fun handleAuthEvent() {
        coroutineScope.launch {
            authEventBus.event.collect { event ->
                when (event) {
                    AuthEvent.AuthExpired -> {
                        launch { tokenRepository.clearJwt() }
                        launch { appSettingRepository.clearUserId() }
                        launch { appSettingRepository.clearHasNotInterest() }
                        navController.navigateToLogin()
                    }

                    AuthEvent.Authenticated -> {
                        navController.navigateToHome(
                            navOptions = navOptions {
                                popUpTo(0) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        )
                        delay(30)
                        shouldShowSplash = false
                    }

                    AuthEvent.NotAuthenticated -> {
                        shouldShowSplash = false
                    }

                    AuthEvent.SignOut -> {
                        navController.navigateToLogin()
                    }
                }
            }
        }
    }

    private fun checkAuthentication() {
        coroutineScope.launch {
            val accessToken = tokenRepository.getAccessToken()
            val refreshToken = tokenRepository.getRefreshToken()
            val hasNotInterest = appSettingRepository.hasNotInterest()

            if (accessToken == null || refreshToken == null || hasNotInterest) {
                authEventBus.notifyNotAuthenticated()
            } else {
                authEventBus.notifyAuthenticated()
            }
        }
    }
}