package com.onmoim.feature.groups.view.mygroup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.Group
import com.onmoim.core.designsystem.component.CommonTab
import com.onmoim.core.designsystem.component.CommonTabRow
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.MyGroupTab
import com.onmoim.feature.groups.state.JoinedGroupUiState
import com.onmoim.feature.groups.viewmodel.MyGroupViewModel

@Composable
fun MyGroupRoute(
    myGroupViewModel: MyGroupViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    onNavigateToGroupCategorySelect: () -> Unit,
    onNavigateToComingSchedule: () -> Unit,
    onNavigateToGroupDetail: (groupId: Int) -> Unit
) {
    val selectedTab by myGroupViewModel.selectedTabState.collectAsStateWithLifecycle()
    val joinedGroupUiState by myGroupViewModel.joinedGroupUiState.collectAsStateWithLifecycle()

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
            onTabChange = myGroupViewModel::onSelectedTabChange,
            onClickCreateGroup = onNavigateToGroupCategorySelect,
            onClickComingSchedule = onNavigateToComingSchedule,
            joinedGroupUiState = joinedGroupUiState,
            onClickGroup = onNavigateToGroupDetail
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
    onClickComingSchedule: () -> Unit,
    joinedGroupUiState: JoinedGroupUiState,
    onClickGroup: (groupId: Int) -> Unit
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
                    onClickComingSchedule = onClickComingSchedule,
                    joinedGroupUiState = joinedGroupUiState,
                    onClickGroup = onClickGroup
                )
            }

            MyGroupTab.GROUP_CHAT -> {

            }
        }
    }
}


@Preview
@Composable
private fun MyGroupScreenForMyGroupPreview() {
    val sampleGroup = Group(
        id = 1,
        imageUrl = "",
        title = "Sample Group",
        location = "Sample Location",
        memberCount = 10,
        scheduleCount = 5,
        categoryName = "Sample Category",
        memberStatus = MemberStatus.MEMBER,
        isFavorite = false,
        isRecommend = false
    )

    OnmoimTheme {
        MyGroupScreen(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxSize(),
            selectedTab = MyGroupTab.MY_GROUP,
            onTabChange = {},
            onClickCreateGroup = {},
            onClickComingSchedule = {},
            joinedGroupUiState = JoinedGroupUiState.Success(listOf(sampleGroup)),
            onClickGroup = {}
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
            onClickComingSchedule = {},
            joinedGroupUiState = JoinedGroupUiState.Loading,
            onClickGroup = {}
        )
    }
}