package com.onmoim.feature.home.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.HomeGroup
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.group.GroupHeader
import com.onmoim.core.designsystem.component.group.GroupItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.home.R
import com.onmoim.feature.home.constant.HomeGroupType
import com.onmoim.feature.home.viewmodel.GroupMoreViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun GroupMoreRoute(
    groupMoreViewModel: GroupMoreViewModel = hiltViewModel(),
    onNavigateToGroupDetail: (id: Int) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    // TODO: api 연동하면 수정
    val groupPagingItems =
        MutableStateFlow(PagingData.empty<HomeGroup>()).collectAsLazyPagingItems()

    GroupMoreScreen(
        homeGroupType = groupMoreViewModel.homeGroupType,
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onClickGroup = onNavigateToGroupDetail,
        groupPagingItems = groupPagingItems
    )
}

@Composable
private fun GroupMoreScreen(
    homeGroupType: HomeGroupType,
    onBack: () -> Unit,
    onClickGroup: (id: Int) -> Unit,
    groupPagingItems: LazyPagingItems<HomeGroup>
) {
    val loadState = groupPagingItems.loadState.refresh

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = OnmoimTheme.colors.backgroundColor
            )
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.more_show),
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
        GroupHeader(
            title = stringResource(
                id = when (homeGroupType) {
                    HomeGroupType.POPULAR_ACTIVE -> R.string.home_popular_active_group
                    HomeGroupType.POPULAR_NEARBY -> R.string.home_popular_nearby_group
                    HomeGroupType.RECOMMEND_NEARBY -> R.string.home_nearby_group
                    HomeGroupType.RECOMMEND_SIMILAR -> R.string.home_similar_interest_group
                }
            ),
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (loadState) {
                is LoadState.Error -> {
                    // TODO: 에러 처리
                }

                LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is LoadState.NotLoading -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 15.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            count = groupPagingItems.itemCount
                        ) {
                            groupPagingItems[it]?.let { item ->
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
                                    isRecommended = false,
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
}

@Preview(showBackground = true)
@Composable
private fun GroupMoreScreenPreview() {
    val sampleGroup = HomeGroup(
        id = 1,
        imageUrl = "",
        title = "Sample Group",
        location = "Sample Location",
        memberCount = 10,
        scheduleCount = 5,
        categoryName = "Sample Category",
        memberStatus = MemberStatus.MEMBER,
        isFavorite = false
    )
    val pagingData = PagingData.from(listOf(sampleGroup, sampleGroup, sampleGroup))
    val flow = MutableStateFlow(pagingData)
    val lazyPagingItems = flow.collectAsLazyPagingItems()

    OnmoimTheme {
        GroupMoreScreen(
            homeGroupType = HomeGroupType.POPULAR_ACTIVE,
            onBack = {},
            onClickGroup = {},
            groupPagingItems = lazyPagingItems
        )
    }
}

