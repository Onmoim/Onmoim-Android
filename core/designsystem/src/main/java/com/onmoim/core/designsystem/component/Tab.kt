package com.onmoim.core.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.NoRippleConfiguration

@Composable
fun CommonTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = OnmoimTheme.colors.backgroundColor,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                height = 2.dp,
                color = OnmoimTheme.colors.textColor
            )
        },
        divider = {
            HorizontalDivider(
                color = OnmoimTheme.colors.gray04,
                thickness = 2.dp
            )
        },
        tabs = tabs
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String
) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides NoRippleConfiguration
    ) {
        Tab(
            selected = selected,
            onClick = onClick,
            modifier = modifier,
            text = {
                Text(
                    text = text,
                    style = OnmoimTheme.typography.body2SemiBold.copy(
                        color = if (selected) {
                            OnmoimTheme.colors.textColor
                        } else {
                            OnmoimTheme.colors.gray04
                        }
                    )
                )
            }
        )
    }
}