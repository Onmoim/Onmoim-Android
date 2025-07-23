package com.onmoim.feature.groups.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.FilterChip
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.group.ComingScheduleCard
import com.onmoim.core.designsystem.component.group.ComingScheduleCardButtonType
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.ComingScheduleFilter
import java.time.LocalDateTime

@Composable
fun ComingScheduleRoute(

) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    ComingScheduleScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onClickReset = {},
        onClickFilter = {},
        selectedFilters = emptySet()
    )
}

@Composable
private fun ComingScheduleScreen(
    onBack: () -> Unit,
    onClickReset: () -> Unit,
    onClickFilter: (ComingScheduleFilter) -> Unit,
    selectedFilters: Set<ComingScheduleFilter>
) {
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
                                ComingScheduleFilter.REGULAR_MEET -> R.string.coming_schedule_regular_meet
                                ComingScheduleFilter.LIGHTNING -> R.string.coming_schedule_lightning
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(2) {
                    ComingScheduleCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClickButton = {},
                        buttonType = ComingScheduleCardButtonType.ATTEND,
                        isLightning = false,
                        startDate = LocalDateTime.now().plusDays(2),
                        title = "퇴근 후 독서 정모: 각자 독서",
                        placeName = "카페 언노운",
                        cost = 1000,
                        joinCount = 6,
                        capacity = 8,
                        imageUrl = "https://picsum.photos/200"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ComingScheduleScreenPreview() {
    OnmoimTheme {
        ComingScheduleScreen(
            onBack = {},
            onClickReset = {},
            onClickFilter = {},
            selectedFilters = emptySet()
        )
    }
}