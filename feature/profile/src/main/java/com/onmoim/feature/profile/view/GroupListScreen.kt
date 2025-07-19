package com.onmoim.feature.profile.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.group.GroupItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.profile.R
import com.onmoim.feature.profile.constant.GroupType

@Composable
fun GroupListRoute(
    groupType: GroupType,
    onNavigateToGroupDetail: (id: Int) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    GroupListScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        groupType = groupType,
        onClickGroup = onNavigateToGroupDetail
    )
}

@Composable
private fun GroupListScreen(
    onBack: () -> Unit,
    groupType: GroupType,
    onClickGroup: (id: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(
                        id = when (groupType) {
                            GroupType.FAVORITE -> R.string.profile_favorit_group
                            GroupType.RECENT -> R.string.profile_recent_group
                            GroupType.JOIN -> R.string.profile_join_group
                        }
                    ),
                    style = OnmoimTheme.typography.body1SemiBold
                )
            },
            modifier = Modifier.background(Color.White),
            navigationIcon = {
                NavigationIconButton(
                    onClick = onBack
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 15.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(20) {
                GroupItem(
                    onClick = {
                        onClickGroup(1)
                    },
                    imageUrl = "https://picsum.photos/200",
                    title = "title",
                    location = "location",
                    memberCount = 123,
                    scheduleCount = 123,
                    categoryName = "categoryName",
                    isRecommended = true,
                    isSignUp = false,
                    isOperating = false,
                    isFavorite = false
                )
            }
        }
    }
}

@Preview
@Composable
private fun GroupListScreenPreview() {
    OnmoimTheme {
        GroupListScreen(
            onBack = {},
            groupType = GroupType.FAVORITE,
            onClickGroup = {}
        )
    }
}