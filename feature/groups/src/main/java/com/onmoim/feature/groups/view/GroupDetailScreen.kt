package com.onmoim.feature.groups.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.designsystem.component.CommonChip
import com.onmoim.core.designsystem.component.CommonTab
import com.onmoim.core.designsystem.component.CommonTabRow
import com.onmoim.core.designsystem.component.groups.ComingScheduleCard
import com.onmoim.core.designsystem.component.groups.GroupDetailAppBar
import com.onmoim.core.designsystem.component.groups.PostCard
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.shimmerBackground
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.GroupDetailPostFilter
import com.onmoim.feature.groups.constant.GroupDetailPostViewMode
import com.onmoim.feature.groups.constant.GroupDetailTab
import com.onmoim.feature.groups.viewmodel.GroupDetailViewModel
import java.time.LocalDateTime

@Composable
fun GroupDetailRoute(
    groupDetailViewModel: GroupDetailViewModel,
    onNavigateToComingSchedule: () -> Unit,
    onNavigateToPostDetail: (id: Int) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var selectedTab by remember { mutableStateOf(GroupDetailTab.HOME) }

    GroupDetailScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        selectedTab = selectedTab,
        onTabChange = {
            selectedTab = it
        },
        onClickComingSchedule = onNavigateToComingSchedule,
        onClickPost = onNavigateToPostDetail
    )
}

@Composable
private fun GroupDetailScreen(
    onBack: () -> Unit,
    selectedTab: GroupDetailTab,
    onTabChange: (GroupDetailTab) -> Unit,
    onClickComingSchedule: () -> Unit,
    onClickPost: (id: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = OnmoimTheme.colors.backgroundColor)
    ) {
        GroupDetailAppBar(
            title = "카페에서 문장 한 모금",
            isFavorite = false,
            onClickBack = onBack,
            onClickFavorite = {},
            onClickShare = {},
            onClickMore = {}
        )
        CommonTabRow(
            selectedTabIndex = GroupDetailTab.entries.indexOf(selectedTab),
            modifier = Modifier.height(40.dp),
        ) {
            GroupDetailTab.entries.forEach { tab ->
                CommonTab(
                    selected = selectedTab == tab,
                    onClick = {
                        onTabChange(tab)
                    },
                    modifier = Modifier.height(40.dp),
                    text = stringResource(
                        id = when (tab) {
                            GroupDetailTab.HOME -> R.string.group_detail_tab_home
                            GroupDetailTab.POST -> R.string.group_detail_tab_post
                            GroupDetailTab.CHAT -> R.string.group_detail_tab_chat
                        }
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedTab) {
                GroupDetailTab.HOME -> {
                    GroupDetailHomeContainer(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        title = "카페에서 문장 한 모금",
                        imageUrl = "https://picsum.photos/200",
                        location = "연남동",
                        category = "인문학/책/글",
                        memberCount = 123,
                        content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                        onClickComingSchedule = onClickComingSchedule,
                        onClickAttend = {}
                    )
                }

                GroupDetailTab.POST -> {
                    GroupDetailPostContainer(
                        onClickPost = onClickPost,
                        modifier = Modifier.fillMaxSize(),
                        selectedFilter = GroupDetailPostFilter.ALL,
                        onFilterChange = {}
                    )
                }

                GroupDetailTab.CHAT -> {}
            }
        }
    }
}

@Composable
private fun GroupDetailHomeContainer(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String,
    location: String,
    category: String,
    memberCount: Int,
    content: String,
    onClickComingSchedule: () -> Unit,
    onClickAttend: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(imageUrl)
        }.build()
    )
    val painterState by painter.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        Crossfade(
            targetState = painterState,
            modifier = Modifier.aspectRatio(20 / 9f)
        ) { state ->
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmerBackground()
                    )
                }

                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = state.painter,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFA4A4A4))
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CommonChip(location)
            CommonChip(category)
            CommonChip(stringResource(R.string.group_detail_chip_member_count, memberCount))
        }
        Text(
            text = title,
            modifier = Modifier
                .padding(
                    vertical = 21.5.dp,
                    horizontal = 16.dp
                )
                .fillMaxWidth(),
            style = OnmoimTheme.typography.body1SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Text(
            text = content,
            modifier = Modifier
                .padding(
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 20.dp
                )
                .fillMaxWidth(),
            style = OnmoimTheme.typography.body2Regular.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Row(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickComingSchedule
                )
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 21.5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.group_detail_coming_schedule),
                style = OnmoimTheme.typography.body1SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = OnmoimTheme.colors.gray01
                )
                .padding(
                    top = 20.dp,
                    start = 15.dp,
                    end = 15.dp
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // TODO: api 연동시 수정
            List(2) {
                ComingScheduleCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClickAttend = {
                        onClickAttend()
                    },
                    isLightning = false,
                    meetDateTime = LocalDateTime.now().plusDays(2),
                    title = "퇴근 후 독서 정모: 각자 독서",
                    location = "카페 언노운",
                    cost = 10000,
                    currentNumberOfPeople = 6,
                    maxNumberOfPeople = 8,
                    imageUrl = "https://picsum.photos/200",
                )
            }
        }
    }
}

@Composable
private fun GroupDetailPostContainer(
    onClickPost: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedFilter: GroupDetailPostFilter,
    onFilterChange: (GroupDetailPostFilter) -> Unit,
    initialViewMode: GroupDetailPostViewMode = GroupDetailPostViewMode.POST
) {
    var selectedViewMode by remember { mutableStateOf(initialViewMode) }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = OnmoimTheme.colors.gray01
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GroupDetailPostFilter.entries.forEach { filter ->
                Text(
                    text = stringResource(
                        id = when (filter) {
                            GroupDetailPostFilter.ALL -> R.string.group_detail_post_all
                            GroupDetailPostFilter.NOTICE -> R.string.group_detail_post_notice
                            GroupDetailPostFilter.REG_GREETING -> R.string.group_detail_post_reg_greeting
                            GroupDetailPostFilter.MEET_REVIEW -> R.string.group_detail_post_meet_review
                            GroupDetailPostFilter.FREE_BOARD -> R.string.group_detail_post_free_board
                        }
                    ),
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                onFilterChange(filter)
                            }
                        )
                        .padding(horizontal = 15.5.dp, vertical = 15.dp),
                    style = OnmoimTheme.typography.caption1Regular.copy(
                        color = if (selectedFilter == filter) {
                            OnmoimTheme.colors.textColor
                        } else {
                            OnmoimTheme.colors.gray04
                        }
                    )
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            selectedViewMode = when (selectedViewMode) {
                                GroupDetailPostViewMode.POST -> GroupDetailPostViewMode.ALBUM
                                GroupDetailPostViewMode.ALBUM -> GroupDetailPostViewMode.POST
                            }
                        }
                    )
                    .padding(horizontal = 15.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(
                        id = when (selectedViewMode) {
                            GroupDetailPostViewMode.POST -> R.string.group_detail_post_view_mode_post
                            GroupDetailPostViewMode.ALBUM -> R.string.group_detail_post_view_mode_album
                        }
                    ),
                    style = OnmoimTheme.typography.caption2Regular.copy(
                        color = OnmoimTheme.colors.gray06
                    )
                )
            }
        }
        when (selectedViewMode) {
            GroupDetailPostViewMode.POST -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(10) {
                        PostCard(
                            onClick = {
                                // TODO: api 연동시 수정
                                onClickPost(it)
                            },
                            userName = "userName",
                            profileImgUrl = "https://picsum.photos/200",
                            writeDateTime = LocalDateTime.now(),
                            title = "title",
                            content = "content",
                            likeCount = 10,
                            commentCount = 20,
                            representImageUrl = "https://picsum.photos/200"
                        )
                    }
                }
            }

            GroupDetailPostViewMode.ALBUM -> {
                LazyVerticalGrid(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(10) {
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current).apply {
                                data("https://picsum.photos/200")
                            }.build()
                        )
                        val painterState by painter.state.collectAsStateWithLifecycle()

                        Crossfade(
                            targetState = painterState,
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        // TODO: api 연동시 수정
                                        onClickPost(it)
                                    }
                                )
                                .aspectRatio(1f)
                        ) { state ->
                            when (state) {
                                is AsyncImagePainter.State.Loading -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .shimmerBackground()
                                    )
                                }

                                is AsyncImagePainter.State.Success -> {
                                    Image(
                                        painter = state.painter,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                else -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(0xFFA4A4A4))
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

@Preview
@Composable
private fun GroupDetailScreenForHomePreview() {
    OnmoimTheme {
        GroupDetailScreen(
            onBack = {},
            selectedTab = GroupDetailTab.HOME,
            onTabChange = {},
            onClickComingSchedule = {},
            onClickPost = {}
        )
    }
}

@Preview
@Composable
private fun GroupDetailScreenForPostPreview() {
    OnmoimTheme {
        GroupDetailScreen(
            onBack = {},
            selectedTab = GroupDetailTab.POST,
            onTabChange = {},
            onClickComingSchedule = {},
            onClickPost = {}
        )
    }
}