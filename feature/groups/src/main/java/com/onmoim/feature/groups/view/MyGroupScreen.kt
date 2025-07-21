package com.onmoim.feature.groups.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.component.CommonTab
import com.onmoim.core.designsystem.component.CommonTabRow
import com.onmoim.core.designsystem.component.DayCard
import com.onmoim.core.designsystem.component.group.ComingScheduleCard
import com.onmoim.core.designsystem.component.group.GroupItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.MyGroupTab
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun MyGroupRoute(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    onNavigateToGroupCategorySelect: () -> Unit,
    onNavigateToComingSchedule: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(MyGroupTab.MY_GROUP) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        topBar()
        MyGroupScreen(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            selectedTab = selectedTab,
            onTabChange = {
                selectedTab = it
            },
            onClickCreateGroup = onNavigateToGroupCategorySelect,
            onClickComingSchedule = onNavigateToComingSchedule
        )
        bottomBar()
    }
}

@Composable
private fun MyGroupScreen(
    modifier: Modifier = Modifier,
    selectedTab: MyGroupTab,
    onTabChange: (MyGroupTab) -> Unit,
    onClickCreateGroup: () -> Unit,
    onClickComingSchedule: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        CommonTabRow(
            selectedTabIndex = MyGroupTab.entries.indexOf(selectedTab),
            modifier = Modifier.height(40.dp),
        ) {
            MyGroupTab.entries.forEach { tab ->
                CommonTab(
                    selected = selectedTab == tab,
                    onClick = {
                        onTabChange(tab)
                    },
                    modifier = Modifier.height(40.dp),
                    text = stringResource(
                        id = when (tab) {
                            MyGroupTab.MY_GROUP -> R.string.my_group
                            MyGroupTab.GROUP_CHAT -> R.string.group_chat
                        }
                    )
                )
            }
        }
        when (selectedTab) {
            MyGroupTab.MY_GROUP -> {
                MyGroupContainer(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    onClickCreateGroup = onClickCreateGroup,
                    onClickComingSchedule = onClickComingSchedule
                )
            }

            MyGroupTab.GROUP_CHAT -> {

            }
        }
    }
}

@Composable
private fun MyGroupContainer(
    modifier: Modifier = Modifier,
    onClickCreateGroup: () -> Unit,
    onClickComingSchedule: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.my_group_joined_group),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 21.5.dp),
            style = OnmoimTheme.typography.body1SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            List(2) {
                GroupItem(
                    onClick = {},
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
        Box(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickCreateGroup
                )
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.my_group_create_group),
                style = OnmoimTheme.typography.body2Regular.copy(
                    color = OnmoimTheme.colors.gray05
                )
            )
        }
        HorizontalDivider(
            thickness = 5.dp,
            color = OnmoimTheme.colors.gray01
        )
        Row(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickComingSchedule
                )
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 21.5.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.coming_schedule),
                style = OnmoimTheme.typography.body1SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(8.dp, 15.dp)
            )
        }
        Column(
            modifier = Modifier
                .background(OnmoimTheme.colors.gray01)
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp),
                contentPadding = PaddingValues(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val now = LocalDate.now()
                val comingDates = buildList {
                    add(now)
                    repeat(6) { index ->
                        add(now.plusDays(index + 1L))
                    }
                }
                items(comingDates) {
                    DayCard(
                        onClick = {},
                        selected = it == now,
                        date = it
                    )
                }
            }
            // TODO: api 연동시 수정
            List(2) {
                ComingScheduleCard(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth(),
                    onClickAttend = {

                    },
                    isLightning = false,
                    startDate = LocalDateTime.now().plusDays(2),
                    title = "퇴근 후 독서 정모: 각자 독서",
                    placeName = "카페 언노운",
                    cost = 10000,
                    joinCount = 6,
                    capacity = 8,
                    imageUrl = "https://picsum.photos/200",
                    attendance = true
                )
            }
        }
    }
}

@Preview
@Composable
private fun MyGroupScreenForMyGroupPreview() {
    OnmoimTheme {
        MyGroupScreen(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxSize(),
            selectedTab = MyGroupTab.MY_GROUP,
            onTabChange = {},
            onClickCreateGroup = {},
            onClickComingSchedule = {}
        )
    }
}

@Preview
@Composable
private fun MyGroupScreenForGroupChatPreview() {
    OnmoimTheme {
        MyGroupScreen(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxSize(),
            selectedTab = MyGroupTab.GROUP_CHAT,
            onTabChange = {},
            onClickCreateGroup = {},
            onClickComingSchedule = {}
        )
    }
}