package com.onmoim.feature.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.component.CommonTab
import com.onmoim.core.designsystem.component.CommonTabRow
import com.onmoim.core.designsystem.component.meet.MeetHeader
import com.onmoim.core.designsystem.component.meet.MeetItem
import com.onmoim.core.designsystem.component.meet.MeetPager
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.home.HomeTab
import com.onmoim.feature.home.R

@Composable
fun HomeRoute(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    var selectedTab by remember { mutableStateOf(HomeTab.RECOMMEND) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        topBar()
        HomeScreen(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            selectedTab = selectedTab,
            onTabChange = { selectedTab = it }
        )
        bottomBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    selectedTab: HomeTab,
    onTabChange: (HomeTab) -> Unit
) {
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
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    MeetPager(
                        title = stringResource(R.string.home_similar_interest),
                        pageSize = 3,
                        itemsPerPage = 4,
                        onClickMore = {
                            // TODO: 모임 더보기 화면으로 이동
                        }
                    ) { page, index ->
                        MeetItem(
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
                    MeetPager(
                        title = stringResource(R.string.home_near_meet),
                        pageSize = 3,
                        itemsPerPage = 4,
                        onClickMore = {
                            // TODO: 모임 더보기 화면으로 이동
                        }
                    ) { page, index ->
                        MeetItem(
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

            HomeTab.POPULARITY -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    MeetPager(
                        title = stringResource(R.string.home_near_popular_meet),
                        pageSize = 3,
                        itemsPerPage = 4,
                        onClickMore = {
                            // TODO: 모임 더보기 화면으로 이동
                        }
                    ) { page, index ->
                        MeetItem(
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
                    MeetPager(
                        title = stringResource(R.string.home_lively_meet),
                        pageSize = 3,
                        itemsPerPage = 4,
                        onClickMore = {
                            // TODO: 모임 더보기 화면으로 이동
                        }
                    ) { page, index ->
                        MeetItem(
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

            HomeTab.FAVORITE -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 15.dp)
                ) {
                    item {
                        MeetHeader(
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
                            MeetItem(
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

@Preview
@Composable
private fun HomeScreenPreview() {
    var selectedTab by remember { mutableStateOf(HomeTab.RECOMMEND) }

    OnmoimTheme {
        HomeScreen(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxSize(),
            selectedTab = selectedTab,
            onTabChange = {
                selectedTab = it
            }
        )
    }
}