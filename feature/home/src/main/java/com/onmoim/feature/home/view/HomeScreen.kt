package com.onmoim.feature.home.view

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.data.model.HomeGroup
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

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    onNavigateToGroupDetail: (id: Int) -> Unit,
    onNavigateToMoreGroup: (HomeGroupType) -> Unit
) {
    var selectedTab by remember { mutableStateOf(HomeTab.RECOMMEND) }
    val recommendGroupUiState by homeViewModel.recommendGroupUiState.collectAsStateWithLifecycle()
    val popularGroupUiState by homeViewModel.popularGroupUiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        topBar()
        HomeScreen(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            selectedTab = selectedTab,
            onTabChange = { selectedTab = it },
            onClickGroup = onNavigateToGroupDetail,
            onClickMore = onNavigateToMoreGroup,
            recommendGroupUiState = recommendGroupUiState,
            popularGroupUiState = popularGroupUiState
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
                        // TODO: 에러 처리
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
                        // TODO: 에러 처리
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
                    items(10) { index ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (index > 0) {
                                Spacer(Modifier.height(16.dp))
                            }
                            GroupItem(
                                onClick = {
                                    // TODO: 모임 상세 화면으로 이동
                                },
                                imageUrl = "https://picsum.photos/200",
                                title = "title",
                                location = "location",
                                memberCount = 123,
                                scheduleCount = 123,
                                categoryName = "categoryName",
                                isRecommended = true,
                                isSignUp = true,
                                isOperating = true,
                                isFavorite = true
                            )
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
    similarGroups: List<HomeGroup>,
    nearbyGroups: List<HomeGroup>
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
                isRecommended = true,
                isSignUp = true,
                isOperating = true,
                isFavorite = true
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
                isRecommended = true,
                isSignUp = true,
                isOperating = true,
                isFavorite = true
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
    nearbyGroups: List<HomeGroup>,
    activeGroups: List<HomeGroup>
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
                isRecommended = true,
                isSignUp = true,
                isOperating = true,
                isFavorite = true
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
                isRecommended = true,
                isSignUp = true,
                isOperating = true,
                isFavorite = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    OnmoimTheme {
        HomeScreen(
            selectedTab = HomeTab.RECOMMEND,
            onTabChange = {},
            onClickGroup = {},
            onClickMore = {},
            recommendGroupUiState = HomeRecommendGroupUiState.Success(
                similarGroups = listOf(
                    HomeGroup(0, "https://picsum.photos/200", "축구 동호회", "서울시 강남구", 10, 5, "스포츠"),
                    HomeGroup(1, "https://picsum.photos/201", "농구 동호회", "서울시 서초구", 15, 3, "스포츠"),
                    HomeGroup(2, "https://picsum.photos/202", "야구 동호회", "서울시 송파구", 20, 2, "스포츠"),
                    HomeGroup(3, "https://picsum.photos/203", "배구 동호회", "서울시 강동구", 12, 4, "스포츠"),
                ),
                nearbyGroups = listOf(
                    HomeGroup(4, "https://picsum.photos/204", "독서 모임", "서울시 강남구", 5, 1, "문화"),
                    HomeGroup(5, "https://picsum.photos/205", "영화 모임", "서울시 서초구", 8, 2, "문화"),
                    HomeGroup(6, "https://picsum.photos/206", "음악 모임", "서울시 송파구", 7, 3, "문화"),
                    HomeGroup(7, "https://picsum.photos/207", "미술 모임", "서울시 강동구", 6, 1, "문화"),
                )
            ),
            popularGroupUiState = HomePopularGroupUiState.Success(
                nearbyGroups = listOf(
                    HomeGroup(8, "https://picsum.photos/208", "코딩 스터디", "서울시 강남구", 10, 5, "IT"),
                    HomeGroup(9, "https://picsum.photos/209", "알고리즘 스터디", "서울시 서초구", 15, 3, "IT"),
                    HomeGroup(10, "https://picsum.photos/210", "CS 스터디", "서울시 송파구", 20, 2, "IT"),
                    HomeGroup(11, "https://picsum.photos/211", "프론트엔드 스터디", "서울시 강동구", 12, 4, "IT"),
                ),
                activeGroups = listOf(
                    HomeGroup(12, "https://picsum.photos/212", "백엔드 스터디", "서울시 강남구", 5, 1, "IT"),
                    HomeGroup(13, "https://picsum.photos/213", "안드로이드 스터디", "서울시 서초구", 8, 2, "IT"),
                    HomeGroup(14, "https://picsum.photos/214", "iOS 스터디", "서울시 송파구", 7, 3, "IT"),
                    HomeGroup(15, "https://picsum.photos/215", "데브옵스 스터디", "서울시 강동구", 6, 1, "IT"),
                )
            )
        )
    }
}