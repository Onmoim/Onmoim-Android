package com.onmoim.feature.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.Group
import com.onmoim.core.designsystem.component.CommonTab
import com.onmoim.core.designsystem.component.CommonTabRow
import com.onmoim.core.designsystem.component.group.GroupHeader
import com.onmoim.core.designsystem.component.group.GroupItem
import com.onmoim.core.designsystem.component.group.GroupPager
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.home.R
import com.onmoim.feature.home.constant.HomeGroupType
import com.onmoim.feature.home.constant.HomeTab
import com.onmoim.feature.home.state.HomePopularGroupUiState
import com.onmoim.feature.home.state.HomeRecommendGroupUiState
import com.onmoim.feature.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    onNavigateToGroupDetail: (id: Int) -> Unit,
    onNavigateToMoreGroup: (HomeGroupType) -> Unit
) {
    val selectedTab by homeViewModel.selectedTabState.collectAsStateWithLifecycle()
    val recommendGroupUiState by homeViewModel.recommendGroupUiState.collectAsStateWithLifecycle()
    val popularGroupUiState by homeViewModel.popularGroupUiState.collectAsStateWithLifecycle()
    val favoriteGroupPagingItems = homeViewModel.favoriteGroupPagingData.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        topBar()
        HomeScreen(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            selectedTab = selectedTab,
            onTabChange = homeViewModel::onSelectedTabChange,
            onClickGroup = onNavigateToGroupDetail,
            onClickMore = onNavigateToMoreGroup,
            recommendGroupUiState = recommendGroupUiState,
            popularGroupUiState = popularGroupUiState,
            favoriteGroupPagingItems = favoriteGroupPagingItems
        )
        bottomBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    selectedTab: HomeTab,
    onTabChange: (HomeTab) -> Unit,
    onClickGroup: (id: Int) -> Unit,
    onClickMore: (HomeGroupType) -> Unit,
    recommendGroupUiState: HomeRecommendGroupUiState,
    popularGroupUiState: HomePopularGroupUiState,
    favoriteGroupPagingItems: LazyPagingItems<Group>
) {
    val itemsPerPage = 4

    Column(
        modifier = modifier
    ) {
        CommonTabRow(
            selectedTabIndex = HomeTab.entries.indexOf(selectedTab),
            modifier = Modifier.height(40.dp),
        ) {
            HomeTab.entries.forEach { tab ->
                CommonTab(
                    selected = selectedTab == tab,
                    onClick = {
                        onTabChange(tab)
                    },
                    modifier = Modifier.height(40.dp),
                    text = stringResource(id = tab.labelId)
                )
            }
        }
        when (selectedTab) {
            HomeTab.RECOMMEND -> {
                when (recommendGroupUiState) {
                    is HomeRecommendGroupUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(recommendGroupUiState.error.message.toString())
                        }
                    }

                    HomeRecommendGroupUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is HomeRecommendGroupUiState.Success -> {
                        RecommendContent(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            onClickDetail = onClickGroup,
                            onClickMore = onClickMore,
                            itemsPerPage = itemsPerPage,
                            similarGroups = recommendGroupUiState.similarGroups,
                            nearbyGroups = recommendGroupUiState.nearbyGroups
                        )
                    }
                }
            }

            HomeTab.POPULARITY -> {
                when (popularGroupUiState) {
                    is HomePopularGroupUiState.Error -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(popularGroupUiState.error.message.toString())
                        }
                    }

                    HomePopularGroupUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is HomePopularGroupUiState.Success -> {
                        PopularContent(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            onClickDetail = onClickGroup,
                            onClickMore = onClickMore,
                            itemsPerPage = itemsPerPage,
                            nearbyGroups = popularGroupUiState.nearbyGroups,
                            activeGroups = popularGroupUiState.activeGroups
                        )
                    }
                }
            }

            HomeTab.FAVORITE -> {
                val loadState = favoriteGroupPagingItems.loadState.refresh

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
                            contentPadding = PaddingValues(horizontal = 15.dp)
                        ) {
                            item {
                                GroupHeader(
                                    title = stringResource(R.string.home_my_favorite_meet)
                                )
                            }
                            items(favoriteGroupPagingItems.itemCount) { index ->
                                favoriteGroupPagingItems[index]?.let { item ->
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        if (index > 0) {
                                            Spacer(Modifier.height(16.dp))
                                        }
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
        }
    }
}

@Composable
private fun RecommendContent(
    modifier: Modifier = Modifier,
    onClickDetail: (id: Int) -> Unit,
    onClickMore: (HomeGroupType) -> Unit,
    itemsPerPage: Int,
    similarGroups: List<Group>,
    nearbyGroups: List<Group>
) {
    val similarGroupsForPage = similarGroups.chunked(itemsPerPage)
    val nearbyGroupsForPage = nearbyGroups.chunked(itemsPerPage)

    Column(
        modifier = modifier
    ) {
        GroupPager(
            title = stringResource(R.string.home_similar_interest_group),
            itemCount = similarGroups.size,
            itemsPerPage = itemsPerPage,
            onClickMore = {
                onClickMore(HomeGroupType.RECOMMEND_SIMILAR)
            }
        ) { page, index ->
            val item = similarGroupsForPage[page][index]

            GroupItem(
                onClick = {
                    onClickDetail(item.id)
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
        GroupPager(
            title = stringResource(R.string.home_nearby_group),
            itemCount = nearbyGroups.size,
            itemsPerPage = itemsPerPage,
            onClickMore = {
                onClickMore(HomeGroupType.RECOMMEND_NEARBY)
            }
        ) { page, index ->
            val item = nearbyGroupsForPage[page][index]

            GroupItem(
                onClick = {
                    onClickDetail(item.id)
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

@Composable
private fun PopularContent(
    modifier: Modifier = Modifier,
    onClickDetail: (id: Int) -> Unit,
    onClickMore: (HomeGroupType) -> Unit,
    itemsPerPage: Int,
    nearbyGroups: List<Group>,
    activeGroups: List<Group>
) {
    val nearbyGroupsForPage = nearbyGroups.chunked(itemsPerPage)
    val activeGroupsForPage = activeGroups.chunked(itemsPerPage)

    Column(
        modifier = modifier
    ) {
        GroupPager(
            title = stringResource(R.string.home_popular_nearby_group),
            itemCount = nearbyGroups.size,
            itemsPerPage = itemsPerPage,
            onClickMore = {
                onClickMore(HomeGroupType.POPULAR_NEARBY)
            }
        ) { page, index ->
            val item = nearbyGroupsForPage[page][index]

            GroupItem(
                onClick = {
                    onClickDetail(item.id)
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
        GroupPager(
            title = stringResource(R.string.home_popular_active_group),
            itemCount = activeGroups.size,
            itemsPerPage = itemsPerPage,
            onClickMore = {
                onClickMore(HomeGroupType.POPULAR_ACTIVE)
            }
        ) { page, index ->
            val item = activeGroupsForPage[page][index]

            GroupItem(
                onClick = {
                    onClickDetail(item.id)
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

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
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

    OnmoimTheme {
        HomeScreen(
            selectedTab = HomeTab.RECOMMEND,
            onTabChange = {},
            onClickGroup = {},
            onClickMore = {},
            recommendGroupUiState = HomeRecommendGroupUiState.Success(
                similarGroups = fakeGroups,
                nearbyGroups = fakeGroups
            ),
            popularGroupUiState = HomePopularGroupUiState.Success(
                nearbyGroups = fakeGroups,
                activeGroups = fakeGroups
            ),
            favoriteGroupPagingItems = flowOf(PagingData.from(fakeGroups)).collectAsLazyPagingItems()
        )
    }
}