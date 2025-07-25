package com.onmoim.feature.groups.view.groupdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.MeetingDetail
import com.onmoim.core.designsystem.component.CommonChip
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.group.ComingScheduleCard
import com.onmoim.core.designsystem.component.group.ComingScheduleCardButtonType
import com.onmoim.core.designsystem.component.group.GroupImageBox
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R

@Composable
fun GroupDetailHomeContainer(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String?,
    location: String,
    category: String,
    memberCount: Int,
    description: String,
    meetings: List<MeetingDetail>,
    memberStatus: MemberStatus,
    onClickComingSchedule: () -> Unit,
    onClickMeetAttend: (id: Int) -> Unit,
    onClickMeetLeave: (id: Int) -> Unit,
    onClickGroupSetting: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxWidth()
        ) {
            GroupImageBox(
                onClick = onClickGroupSetting,
                modifier = Modifier.aspectRatio(20 / 9f),
                imageUrl = imageUrl
            )
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
                if (memberStatus == MemberStatus.OWNER) {
                    CommonChip(
                        label = stringResource(R.string.group_detail_chip_operating),
                        backgroundColor = OnmoimTheme.colors.accentSoftRed,
                        textColor = Color.White
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 6.dp)
                    .fillMaxWidth()
                    .height(62.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = OnmoimTheme.typography.body1SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                if (memberStatus == MemberStatus.OWNER) {
                    NavigationIconButton(
                        onClick = onClickGroupSetting
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_setting),
                            contentDescription = null
                        )
                    }
                }
            }
            Text(
                text = description,
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
                    text = stringResource(R.string.coming_schedule),
                    style = OnmoimTheme.typography.body1SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (meetings.isEmpty()) {
                Text(
                    text = stringResource(R.string.group_detail_no_meeting),
                    modifier = Modifier
                        .padding(vertical = 60.dp)
                        .align(Alignment.CenterHorizontally),
                    style = OnmoimTheme.typography.body2Regular.copy(
                        color = OnmoimTheme.colors.gray04
                    )
                )
            } else {
                meetings.forEach { meet ->
                    ComingScheduleCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClickButton = {
                            if (meet.attendance) {
                                onClickMeetLeave(meet.id)
                            } else {
                                onClickMeetAttend(meet.id)
                            }
                        },
                        buttonType = if (meet.attendance) {
                            ComingScheduleCardButtonType.ATTEND_CANCEL
                        } else {
                            ComingScheduleCardButtonType.ATTEND
                        },
                        isLightning = meet.isLightning,
                        startDate = meet.startDate,
                        title = meet.title,
                        placeName = meet.placeName,
                        cost = meet.cost,
                        joinCount = meet.joinCount,
                        capacity = meet.capacity,
                        imageUrl = meet.imgUrl
                    )
                }
            }
        }
    }
}