package com.onmoim.feature.profile.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.Group
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.group.GroupItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.profile.R
import com.onmoim.feature.profile.constant.GroupType
import com.onmoim.feature.profile.viewmodel.GroupListViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun GroupListRoute(
    groupListViewModel: GroupListViewModel,
    onNavigateToGroupDetail: (id: Int) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val groupPagingItems = groupListViewModel.groupPagingData.collectAsLazyPagingItems()

    GroupListScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        groupType = groupListViewModel.groupType,
        onClickGroup = onNavigateToGroupDetail,
        groupPagingItems = groupPagingItems
    )
}

@Composable
private fun GroupListScreen(
    onBack: () -> Unit,
    groupType: GroupType,
    onClickGroup: (id: Int) -> Unit,
    groupPagingItems: LazyPagingItems<Group>
) {
    val loadState = groupPagingItems.loadState.refresh

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
        when (loadState) {
            is LoadState.Error -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(loadState.error.message.toString())
                }
            }

            LoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.NotLoading -> {
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
                    items(groupPagingItems.itemCount) { index ->
                        groupPagingItems[index]?.let { item ->
                            GroupItem(
                                onClick = {
                                    onClickGroup(item.id)
                                },
                                imageUrl = item.imageUrl,
                                title = item.title,
                                location = item.location,
                                memberCount = item.memberCount,
                                scheduleCount = item.scheduleCount,
                                categoryName = item.categoryName,
                                isRecommended = item.isRecommend,
                                isSignUp = item.memberStatus == MemberStatus.MEMBER,
                                isOperating = item.memberStatus == MemberStatus.OWNER,
                                isFavorite = item.isFavorite
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GroupListScreenPreview() {
    val fakeGroups = List(20) {
        Group(
            it + 1,
            "https://picsum.photos/200",
            "제목 제목 $it",
            "지역 $it",
            10,
            5,
            "카테고리 $it",
            if (it % 2 == 0) MemberStatus.MEMBER else MemberStatus.OWNER,
            it % 2 == 0,
            it % 2 == 0
        )
    }
    val fakePagingItems = MutableStateFlow(PagingData.from(fakeGroups)).collectAsLazyPagingItems()

    OnmoimTheme {
        GroupListScreen(
            onBack = {},
            groupType = GroupType.FAVORITE,
            onClickGroup = {},
            groupPagingItems = fakePagingItems
        )
    }
}