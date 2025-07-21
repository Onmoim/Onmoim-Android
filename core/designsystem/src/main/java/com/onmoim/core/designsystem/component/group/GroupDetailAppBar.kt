package com.onmoim.core.designsystem.component.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.designsystem.theme.TopAppBarHeight

@Composable
fun GroupDetailAppBar(
    title: String,
    isFavorite: Boolean,
    onClickBack: () -> Unit,
    onClickFavorite: () -> Unit,
    onClickShare: () -> Unit,
    onClickMenu: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopAppBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationIconButton(
            onClick = onClickBack
        ) {
            Icon(
                painter = painterResource(com.onmoim.core.ui.R.drawable.ic_arrow_back),
                contentDescription = null
            )
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = OnmoimTheme.typography.body1SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationIconButton(
                onClick = onClickFavorite,
                size = 36.dp
            ) {
                Image(
                    painter = painterResource(
                        id = if (isFavorite) {
                            R.drawable.ic_favorite_enabled
                        } else {
                            R.drawable.ic_favorite_disabled_fill
                        }
                    ),
                    contentDescription = null
                )
            }
            NavigationIconButton(
                onClick = onClickShare,
                size = 36.dp
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = null
                )
            }
            NavigationIconButton(
                onClick = onClickMenu,
                size = 36.dp
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 360)
private fun GroupDetailAppBarPreview() {
    OnmoimTheme {
        GroupDetailAppBar(
            title = "Group Name",
            isFavorite = false,
            onClickBack = {},
            onClickFavorite = {},
            onClickShare = {},
            onClickMenu = {}
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 360)
private fun GroupDetailAppBarFavoritePreview() {
    OnmoimTheme {
        GroupDetailAppBar(
            title = "Group Name",
            isFavorite = true,
            onClickBack = {},
            onClickFavorite = {},
            onClickShare = {},
            onClickMenu = {}
        )
    }
}