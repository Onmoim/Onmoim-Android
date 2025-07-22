package com.onmoim.feature.groups.view.groupmanagement

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.onmoim.core.data.constant.GroupMemberRole
import com.onmoim.core.data.model.ActiveStatistics
import com.onmoim.core.data.model.Member
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonTab
import com.onmoim.core.designsystem.component.CommonTabRow
import com.onmoim.core.ui.LoadingOverlayBox
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.GroupManagementTab
import com.onmoim.feature.groups.state.GroupManagementEvent
import com.onmoim.feature.groups.viewmodel.GroupManagementViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun GroupManagementRoute(
    groupManagementViewModel: GroupManagementViewModel,
    onBackAndRefresh: () -> Unit,
    onNavigateToGroupEdit: () -> Unit,
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val selectedTab by groupManagementViewModel.selectedTabState.collectAsStateWithLifecycle()
    val activeStatistics by groupManagementViewModel.activeStatisticsState.collectAsStateWithLifecycle()
    val groupMemberPagingItems =
        groupManagementViewModel.groupMemberPagingData.collectAsLazyPagingItems()
    val userId by groupManagementViewModel.userIdState.collectAsStateWithLifecycle()
    val isLoading by groupManagementViewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LoadingOverlayBox(
        loading = isLoading,
        modifier = Modifier.fillMaxSize()
    ) {
        GroupManagementScreen(
            onBack = {
                onBackPressedDispatcher?.onBackPressed()
            },
            selectedTab = selectedTab,
            onTabChange = groupManagementViewModel::onTabChange,
            activeStatistics = activeStatistics,
            groupMemberPagingItems = groupMemberPagingItems,
            userId = userId,
            onClickTransfer = groupManagementViewModel::transferOwner,
            onClickBan = groupManagementViewModel::banMember,
            onClickGroupEdit = onNavigateToGroupEdit,
            onClickScheduleManagement = {},
            onClickCreateMeet = {},
            onClickWriteNotice = {},
            onClickGroupDelete = {}
        )
    }

    LaunchedEffect(Unit) {
        groupManagementViewModel.event.collect { event ->
            when (event) {
                is GroupManagementEvent.BanFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                GroupManagementEvent.BanSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.group_management_ban_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is GroupManagementEvent.TransferOwnerFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                GroupManagementEvent.TransferOwnerSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.group_management_transfer_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackAndRefresh()
                }
            }
        }
    }
}

@Composable
private fun GroupManagementScreen(
    onBack: () -> Unit,
    selectedTab: GroupManagementTab,
    onTabChange: (GroupManagementTab) -> Unit,
    activeStatistics: ActiveStatistics?,
    groupMemberPagingItems: LazyPagingItems<Member>,
    userId: Int?,
    onClickTransfer: (memberId: Int) -> Unit,
    onClickBan: (memberId: Int) -> Unit,
    onClickGroupEdit: () -> Unit,
    onClickScheduleManagement: () -> Unit,
    onClickCreateMeet: () -> Unit,
    onClickWriteNotice: () -> Unit,
    onClickGroupDelete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.group_management_page),
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
        CommonTabRow(
            selectedTabIndex = GroupManagementTab.entries.indexOf(selectedTab),
            modifier = Modifier.height(40.dp),
        ) {
            GroupManagementTab.entries.forEach { tab ->
                CommonTab(
                    selected = selectedTab == tab,
                    onClick = {
                        onTabChange(tab)
                    },
                    modifier = Modifier.height(40.dp),
                    text = stringResource(
                        id = when (tab) {
                            GroupManagementTab.ACTIVE_STATUS -> R.string.group_management_active_status
                            GroupManagementTab.GROUP_SETTING -> R.string.group_management_group_setting
                        }
                    )
                )
            }
        }
        when (selectedTab) {
            GroupManagementTab.ACTIVE_STATUS -> {
                ActiveStatusContainer(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    yearlyScheduleCount = activeStatistics?.yearlyScheduleCount ?: 0,
                    monthlyScheduleCount = activeStatistics?.monthlyScheduleCount ?: 0,
                    groupMemberPagingItems = groupMemberPagingItems,
                    userId = userId,
                    onClickBan = onClickBan,
                    onClickTransfer = onClickTransfer
                )
            }

            GroupManagementTab.GROUP_SETTING -> {
                GroupSettingContainer(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    onClickGroupEdit = onClickGroupEdit,
                    onClickScheduleManagement = onClickScheduleManagement,
                    onClickCreateMeet = onClickCreateMeet,
                    onClickWriteNotice = onClickWriteNotice,
                    onClickGroupDelete = onClickGroupDelete
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupManagementScreenForActiveStatusPreview() {
    val members = listOf(
        Member(id = 1, name = "홍길동", profileImageUrl = "", role = GroupMemberRole.OWNER),
        Member(id = 2, name = "김길동", profileImageUrl = "", role = GroupMemberRole.MEMBER),
        Member(id = 3, name = "고길동", profileImageUrl = "", role = GroupMemberRole.MEMBER),
    )
    val pagingItems = flowOf(PagingData.from(members)).collectAsLazyPagingItems()

    OnmoimTheme {
        GroupManagementScreen(
            onBack = {},
            selectedTab = GroupManagementTab.ACTIVE_STATUS,
            onTabChange = {},
            activeStatistics = ActiveStatistics(
                yearlyScheduleCount = 12,
                monthlyScheduleCount = 3,
            ),
            groupMemberPagingItems = pagingItems,
            userId = 1,
            onClickTransfer = {},
            onClickBan = {},
            onClickGroupEdit = {},
            onClickScheduleManagement = {},
            onClickCreateMeet = {},
            onClickWriteNotice = {},
            onClickGroupDelete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupManagementScreenForGroupSettingPreview() {
    val pagingItems = flowOf(PagingData.empty<Member>()).collectAsLazyPagingItems()

    OnmoimTheme {
        GroupManagementScreen(
            onBack = {},
            selectedTab = GroupManagementTab.GROUP_SETTING,
            onTabChange = {},
            activeStatistics = ActiveStatistics(
                yearlyScheduleCount = 12,
                monthlyScheduleCount = 3,
            ),
            groupMemberPagingItems = pagingItems,
            userId = 1,
            onClickTransfer = {},
            onClickBan = {},
            onClickGroupEdit = {},
            onClickScheduleManagement = {},
            onClickCreateMeet = {},
            onClickWriteNotice = {},
            onClickGroupDelete = {}
        )
    }
}