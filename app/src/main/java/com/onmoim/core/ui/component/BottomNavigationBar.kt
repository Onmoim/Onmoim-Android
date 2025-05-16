package com.onmoim.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.onmoim.core.ui.navigation.CategoryRoute
import com.onmoim.core.ui.navigation.HomeRoute
import com.onmoim.core.ui.navigation.MyMeetingRoute
import com.onmoim.core.ui.navigation.ProfileRoute
import com.onmoim.core.ui.navigation.topLevelRoutes
import com.onmoim.core.ui.theme.OnmoimTheme

@Composable
fun BottomNavigationBar(
    currentDestination: NavDestination?,
    onClick: (Any) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = OnmoimTheme.colors.backgroundColor
            )
            .fillMaxWidth()
            .windowInsetsPadding(
                WindowInsets.systemBars.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            )
            .padding(
                start = 21.dp,
                end = 21.dp,
                top = 8.dp,
                bottom = 12.dp
            )
            .selectableGroup(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        topLevelRoutes.forEach { topLevelRoute ->
            val selected = currentDestination?.hierarchy?.any {
                it.hasRoute(topLevelRoute.route::class)
            } == true

            Column(
                modifier = Modifier
                    .selectable(
                        selected = selected,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        role = Role.Tab,
                        onClick = {
                            onClick(topLevelRoute.route)
                        }
                    )
                    .size(42.dp)
                    .padding(
                        top = when (topLevelRoute.route) {
                            is HomeRoute -> 4.dp
                            is CategoryRoute -> 6.dp
                            is MyMeetingRoute -> 4.dp
                            is ProfileRoute -> 3.dp
                            else -> 0.dp
                        }
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        id = if (selected) {
                            topLevelRoute.selectedIconId
                        } else {
                            topLevelRoute.unselectedIconId
                        }
                    ),
                    contentDescription = stringResource(topLevelRoute.labelId)
                )
                BasicText(
                    text = stringResource(topLevelRoute.labelId),
                    style = OnmoimTheme.typography.caption3Regular.copy(
                        color = if (selected) {
                            OnmoimTheme.colors.textColor
                        } else {
                            OnmoimTheme.colors.gray05
                        }
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    OnmoimTheme {
        BottomNavigationBar(
            currentDestination = null,
            onClick = {}
        )
    }
}