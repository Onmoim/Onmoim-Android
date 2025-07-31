package com.onmoim.feature.groups.view

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.onmoim.core.data.constant.MeetingType
import com.onmoim.core.data.model.Meeting
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.FilterChip
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.group.ComingScheduleCard
import com.onmoim.core.designsystem.component.group.ComingScheduleCardButtonType
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.ComingScheduleFilter
import com.onmoim.feature.groups.state.ComingScheduleEvent
import com.onmoim.feature.groups.viewmodel.ComingScheduleViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

@Composable
fun ComingScheduleRoute(
    comingScheduleViewModel: ComingScheduleViewModel,
    onNavigateToCreateSchedule: () -> Unit,
    onNavigateToMeetingLocation: (meetingId: Int) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val selectedFilters by comingScheduleViewModel.filtersState.collectAsStateWithLifecycle()
    val comingSchedulePagingDataFlow by comingScheduleViewModel.comingSchedulePagingDataState.collectAsStateWithLifecycle()
    val comingSchedulePagingItems = comingSchedulePagingDataFlow.collectAsLazyPagingItems()
    val cachedAttendMeetingIds by comingScheduleViewModel.cachedAttendMeetingIdsState.collectAsStateWithLifecycle()
    val cachedLeaveMeetingIds by comingScheduleViewModel.cachedLeaveMeetingIdsState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ComingScheduleScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onClickReset = comingScheduleViewModel::clearFilter,
        onClickFilter = comingScheduleViewModel::onFilterChange,
        selectedFilters = selectedFilters,
        showAddScheduleButton = comingScheduleViewModel.groupId != null,
        onClickCreateSchedule = onNavigateToCreateSchedule,
        onClickSchedule = onNavigateToMeetingLocation,
        comingSchedulePagingItems = comingSchedulePagingItems,
        cachedAttendMeetingIds = cachedAttendMeetingIds,
        cachedLeaveMeetingIds = cachedLeaveMeetingIds,
        onClickAttend = comingScheduleViewModel::attendMeeting,
        onClickLeave = comingScheduleViewModel::leaveMeeting
    )

    LaunchedEffect(Unit) {
        comingScheduleViewModel.event.collect { event ->
            when (event) {
                is ComingScheduleEvent.AttendMeetingFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                ComingScheduleEvent.AttendMeetingOverCapacity -> {
                    Toast.makeText(
                        context,
                        R.string.attend_meeting_over_capacity,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                ComingScheduleEvent.AttendMeetingSuccess -> {
                    Toast.makeText(context, R.string.attend_meeting_success, Toast.LENGTH_SHORT)
                        .show()
                }

                is ComingScheduleEvent.FilterChangeAndLoadDateFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                is ComingScheduleEvent.LeaveMeetingFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                ComingScheduleEvent.LeaveMeetingSuccess -> {
                    Toast.makeText(context, R.string.leave_meeting_success, Toast.LENGTH_SHORT)
                        .show()
                }

                ComingScheduleEvent.MeetingNotFound -> {
                    Toast.makeText(context, R.string.meeting_not_found, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun ComingScheduleScreen(
    onBack: () -> Unit,
    onClickReset: () -> Unit,
    onClickFilter: (ComingScheduleFilter) -> Unit,
    selectedFilters: Set<ComingScheduleFilter>,
    showAddScheduleButton: Boolean,
    onClickCreateSchedule: () -> Unit,
    onClickSchedule: (meetingId: Int) -> Unit,
    comingSchedulePagingItems: LazyPagingItems<Meeting>,
    cachedAttendMeetingIds: Set<Int>,
    cachedLeaveMeetingIds: Set<Int>,
    onClickAttend: (meetingId: Int, groupId: Int) -> Unit,
    onClickLeave: (meetingId: Int, groupId: Int) -> Unit
) {
    val loadState = comingSchedulePagingItems.loadState.refresh

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.coming_schedule),
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
            },
            actions = {
                if (showAddScheduleButton) {
                    NavigationIconButton(
                        onClick = onClickCreateSchedule
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null
                        )
                    }
                }
            }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(OnmoimTheme.colors.gray01)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                contentPadding = PaddingValues(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedFilters.isNotEmpty()) {
                    item {
                        FilterChip(
                            onClick = onClickReset,
                            label = stringResource(R.string.coming_schedule_reset),
                            selected = false,
                            leadingIcon = painterResource(R.drawable.ic_undo)
                        )
                    }
                }
                items(ComingScheduleFilter.entries) { filter ->
                    FilterChip(
                        onClick = {
                            onClickFilter(filter)
                        },
                        label = stringResource(
                            id = when (filter) {
                                ComingScheduleFilter.WEEK -> R.string.coming_schedule_week
                                ComingScheduleFilter.MONTH -> R.string.coming_schedule_month
                                ComingScheduleFilter.ATTEND -> R.string.coming_schedule_attended
                                ComingScheduleFilter.REGULAR_MEET -> R.string.regular_meet
                                ComingScheduleFilter.LIGHTNING -> R.string.lightning
                            }
                        ),
                        selected = selectedFilters.contains(filter),
                        leadingIcon = when (filter) {
                            ComingScheduleFilter.WEEK -> null
                            ComingScheduleFilter.MONTH -> null
                            ComingScheduleFilter.ATTEND -> null
                            ComingScheduleFilter.REGULAR_MEET -> painterResource(R.drawable.ic_crown)
                            ComingScheduleFilter.LIGHTNING -> painterResource(R.drawable.ic_lightning)
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (loadState) {
                    is LoadState.Error -> {
                        Text(loadState.error.message.toString())
                    }

                    LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is LoadState.NotLoading -> {
                        LazyColumn(
                            modifier = Modifier.matchParentSize(),
                            contentPadding = PaddingValues(
                                start = 15.dp,
                                end = 15.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                        ) {
                            items(comingSchedulePagingItems.itemCount) { index ->
                                comingSchedulePagingItems[index]?.let { meeting ->
                                    ComingScheduleCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClickButton = {
                                            when {
                                                cachedAttendMeetingIds.contains(meeting.id) -> {
                                                    onClickLeave(meeting.id, meeting.groupId)
                                                }

                                                cachedLeaveMeetingIds.contains(meeting.id) -> {
                                                    onClickAttend(meeting.id, meeting.groupId)
                                                }

                                                meeting.attendance -> {
                                                    onClickLeave(meeting.id, meeting.groupId)
                                                }

                                                else -> {
                                                    onClickAttend(meeting.id, meeting.groupId)
                                                }
                                            }
                                        },
                                        buttonType = when {
                                            cachedAttendMeetingIds.contains(meeting.id) -> {
                                                ComingScheduleCardButtonType.ATTEND_CANCEL
                                            }

                                            cachedLeaveMeetingIds.contains(meeting.id) -> {
                                                ComingScheduleCardButtonType.ATTEND
                                            }

                                            meeting.attendance -> {
                                                ComingScheduleCardButtonType.ATTEND_CANCEL
                                            }

                                            else -> {
                                                ComingScheduleCardButtonType.ATTEND
                                            }
                                        },
                                        isLightning = meeting.type == MeetingType.LIGHTNING,
                                        startDate = meeting.startDate,
                                        title = meeting.title,
                                        placeName = meeting.placeName,
                                        cost = meeting.cost,
                                        joinCount = meeting.joinCount,
                                        capacity = meeting.capacity,
                                        imageUrl = meeting.imgUrl,
                                        onClickCard = {
                                            onClickSchedule(meeting.id)
                                        }
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
private fun ComingScheduleScreenPreview() {
    val dummyMeetings = List(3) {
        Meeting(
            id = it,
            groupId = it,
            title = "title $it",
            placeName = "place name $it",
            startDate = LocalDateTime.now().plusDays(it.toLong()),
            cost = 0,
            joinCount = 5,
            capacity = 10,
            type = MeetingType.REGULAR,
            imgUrl = null,
            latitude = 0.0,
            longitude = 0.0,
            attendance = false
        )
    }
    val dummyPagingItems =
        MutableStateFlow(PagingData.from(dummyMeetings)).collectAsLazyPagingItems()

    OnmoimTheme {
        ComingScheduleScreen(
            onBack = {},
            onClickReset = {},
            onClickFilter = {},
            selectedFilters = emptySet(),
            showAddScheduleButton = true,
            onClickCreateSchedule = {},
            onClickSchedule = {},
            comingSchedulePagingItems = dummyPagingItems,
            cachedAttendMeetingIds = emptySet(),
            cachedLeaveMeetingIds = emptySet(),
            onClickAttend = { _, _ -> },
            onClickLeave = { _, _ -> }
        )
    }
}