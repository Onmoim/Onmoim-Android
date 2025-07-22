package com.onmoim.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.R

@Composable
fun TopLevelAppBar(
    dongTitle: String,
    onClickDong: () -> Unit,
    onClickSearch: () -> Unit,
    onClickNotification: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(OnmoimTheme.colors.backgroundColor)
            .statusBarsPadding()
            .fillMaxWidth()
            .height(39.dp)
            .padding(
                start = 16.dp,
                end = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClickDong
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = dongTitle,
                style = OnmoimTheme.typography.title3Bold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationIconButton(
                onClick = onClickSearch,
                size = 36.dp
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search_20),
                    contentDescription = null
                )
            }
            NavigationIconButton(
                onClick = onClickNotification,
                size = 36.dp
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_notification_20),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopLevelAppBarPreview() {
    OnmoimTheme {
        TopLevelAppBar(
            dongTitle = "연남동",
            onClickDong = {},
            onClickSearch = {},
            onClickNotification = {}
        )
    }
}