package com.onmoim.feature.groups.view.mygroup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.onmoim.core.data.constant.MeetingType
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.designsystem.component.DayCard
import com.onmoim.core.designsystem.component.group.ComingScheduleCard
import com.onmoim.core.designsystem.component.group.ComingScheduleCardButtonType
import com.onmoim.core.designsystem.component.group.GroupItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.state.JoinedGroupUiState
import com.onmoim.feature.groups.state.UpcomingMeetingUiState
import java.time.LocalDate

@Composable
fun MyGroupContainer(
    modifier: Modifier = Modifier,
    onClickCreateGroup: () -> Unit,
    onClickComingSchedule: () -> Unit,
    joinedGroupUiState: JoinedGroupUiState,
    onClickGroup: (groupId: Int) -> Unit,
    selectedDate: LocalDate,
    onSelectedDateChange: (LocalDate) -> Unit,
    upcomingMeetingUiState: UpcomingMeetingUiState,
    onClickMeetAttend: (meetingId: Int, groupId: Int) -> Unit,
    onClickMeetLeave: (meetingId: Int, groupId: Int) -> Unit,
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
        when (joinedGroupUiState) {
            is JoinedGroupUiState.Error -> {
                Text(joinedGroupUiState.t.message.toString())
            }

            JoinedGroupUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is JoinedGroupUiState.Success -> {
                val groups = joinedGroupUiState.data

                if (groups.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.my_group_joined_group_empty),
                            modifier = Modifier.padding(top = 60.dp, bottom = 80.dp),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.gray04
                            )
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        groups.forEach { item ->
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
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(20.dp))
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
                        onClick = {
                            onSelectedDateChange(it)
                        },
                        selected = it == selectedDate,
                        date = it
                    )
                }
            }
            when (upcomingMeetingUiState) {
                is UpcomingMeetingUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(upcomingMeetingUiState.t.message.toString())
                    }
                }

                UpcomingMeetingUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UpcomingMeetingUiState.Success -> {
                    val meetings = upcomingMeetingUiState.data

                    if (meetings.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Text(
                                text = stringResource(R.string.my_group_joined_group_meeting_empty),
                                modifier = Modifier.padding(vertical = 80.dp),
                                style = OnmoimTheme.typography.body2Regular.copy(
                                    color = OnmoimTheme.colors.gray04
                                )
                            )
                        }

                    } else {
                        meetings.forEachIndexed { index, meeting ->
                            ComingScheduleCard(
                                modifier = Modifier
                                    .padding(horizontal = 15.dp)
                                    .fillMaxWidth(),
                                onClickButton = {
                                    if (meeting.attendance) {
                                        onClickMeetLeave(meeting.id, meeting.groupId)
                                    } else {
                                        onClickMeetAttend(meeting.id, meeting.groupId)
                                    }
                                },
                                buttonType = if (meeting.attendance) {
                                    ComingScheduleCardButtonType.ATTEND_CANCEL
                                } else {
                                    ComingScheduleCardButtonType.ATTEND
                                },
                                isLightning = meeting.type == MeetingType.LIGHTNING,
                                startDate = meeting.startDate,
                                title = meeting.title,
                                placeName = meeting.placeName,
                                cost = meeting.cost,
                                joinCount = meeting.joinCount,
                                capacity = meeting.capacity,
                                imageUrl = meeting.imgUrl
                            )
                            if (index == meetings.lastIndex) {
                                Spacer(Modifier.height(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}