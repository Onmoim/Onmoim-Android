package com.onmoim.feature.groups.view.groupmanagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.onmoim.core.data.constant.GroupMemberRole
import com.onmoim.core.data.model.Member
import com.onmoim.core.designsystem.component.MemberListItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R

@Composable
fun ActiveStatusContainer(
    modifier: Modifier = Modifier,
    yearlyScheduleCount: Int,
    monthlyScheduleCount: Int,
    groupMemberPagingItems: LazyPagingItems<Member>,
    userId: Int?,
    onClickTransfer: (memberId: Int) -> Unit,
    onClickBan: (memberId: Int) -> Unit
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
        items(groupMemberPagingItems.itemCount) { index ->
            groupMemberPagingItems[index]?.let { member ->
                Column {
                    Spacer(Modifier.height(10.dp))
                    MemberListItem(
                        onClickTransfer = {
                            onClickTransfer(member.id)
                        },
                        onClickExpulsion = {
                            onClickBan(member.id)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 15.dp),
                        name = member.name,
                        imageUrl = member.profileImageUrl,
                        isHost = member.role == GroupMemberRole.OWNER,
                        enabledMenu = userId != member.id
                    )
                }
            }
        }
    }
}