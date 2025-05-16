package com.onmoim

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.onmoim.core.ui.component.BottomNavigationBar
import com.onmoim.core.ui.navigation.OnmoimNavHost
import com.onmoim.core.ui.theme.OnmoimTheme

@Composable
fun OnmoimApp(
    appState: OnmoimAppState
) {
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