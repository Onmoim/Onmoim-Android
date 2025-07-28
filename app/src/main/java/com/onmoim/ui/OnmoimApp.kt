package com.onmoim.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.onmoim.core.designsystem.component.TopLevelAppBar
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.navigation.OnmoimNavHost

@Composable
fun OnmoimApp(
    appState: OnmoimAppState
) {
    val userLocation by appState.userLocationState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .background(
                color = OnmoimTheme.colors.backgroundColor
            )
            .fillMaxSize()
    ) {
        OnmoimNavHost(
            navController = appState.navController,
            topBar = {
                TopLevelAppBar(
                    dongTitle = userLocation?.second ?: "",
                    onClickDong = {},
                    onClickSearch = {},
                    onClickNotification = {}
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentDestination = appState.currentDestination,
                    onClick = { route ->
                        appState.navController.navigate(route) {
                            popUpTo(appState.navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            },
        )
    }
}