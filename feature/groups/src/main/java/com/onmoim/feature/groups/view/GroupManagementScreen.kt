package com.onmoim.feature.groups.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.data.model.ActiveStatistics
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonTab
import com.onmoim.core.designsystem.component.CommonTabRow
import com.onmoim.core.designsystem.component.MemberListItem
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.GroupManagementTab
import com.onmoim.feature.groups.viewmodel.GroupManagementViewModel

@Composable
fun GroupManagementRoute(
    groupManagementViewModel: GroupManagementViewModel
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val selectedTab by groupManagementViewModel.selectedTabState.collectAsStateWithLifecycle()
    val activeStatistics by groupManagementViewModel.activeStatisticsState.collectAsStateWithLifecycle()

    GroupManagementScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        selectedTab = selectedTab,
        onTabChange = groupManagementViewModel::onTabChange,
        activeStatistics = activeStatistics
    )
}

@Composable
private fun GroupManagementScreen(
    onBack: () -> Unit,
    selectedTab: GroupManagementTab,
    onTabChange: (GroupManagementTab) -> Unit,
    activeStatistics: ActiveStatistics?
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
                    monthlyScheduleCount = activeStatistics?.monthlyScheduleCount ?: 0
                )
            }

            GroupManagementTab.GROUP_SETTING -> {

            }
        }
    }
}

@Composable
private fun ActiveStatusContainer(
    modifier: Modifier = Modifier,
    yearlyScheduleCount: Int,
    monthlyScheduleCount: Int,
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Column {
                Row(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.group_management_yearly_schedule),
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.gray06
                            )
                        )
                        Text(
                            text = yearlyScheduleCount.toString(),
                            style = OnmoimTheme.typography.title3Bold.copy(
                                color = OnmoimTheme.colors.textColor
                            )
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.group_management_monthly_schedule),
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.gray06
                            )
                        )
                        Text(
                            text = monthlyScheduleCount.toString(),
                            style = OnmoimTheme.typography.title3Bold.copy(
                                color = OnmoimTheme.colors.textColor
                            )
                        )
                    }
                }
                HorizontalDivider(
                    thickness = 5.dp,
                    color = OnmoimTheme.colors.gray01
                )
                Text(
                    text = stringResource(R.string.group_management_member_management),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    style = OnmoimTheme.typography.body2SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
            }
        }
        items(20) {
            Column {
                Spacer(Modifier.height(10.dp))
                MemberListItem(
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    name = "홍길동",
                    imageUrl = null,
                    isHost = false
                )
            }
        }
    }
}

@Preview
@Composable
private fun GroupManagementScreenPreview() {
    OnmoimTheme {
        GroupManagementScreen(
            onBack = {},
            selectedTab = GroupManagementTab.ACTIVE_STATUS,
            onTabChange = {},
            activeStatistics = ActiveStatistics(
                yearlyScheduleCount = 10,
                monthlyScheduleCount = 5
            )
        )
    }
}