package com.onmoim.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.onmoim.core.designsystem.component.TopLevelAppBar
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.navigation.OnmoimNavHost

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
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        OnmoimNavHost(
            navController = appState.navController,
            topBar = {
                TopLevelAppBar(
                    dongTitle = "연남동", // FIXME: 추후 수정 해야함
                    onClickDong = {
                        // TODO: 동 선택?
                    },
                    onClickSearch = {
                        // TODO: 검색 화면으로 이동
                    },
                    onClickNotification = {
                        // TODO: 알림 화면으로 이동
                    }
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