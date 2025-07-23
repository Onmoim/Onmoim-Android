package com.onmoim.feature.groups.view

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonDialog
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.group.ComingScheduleCard
import com.onmoim.core.designsystem.component.group.ComingScheduleCardButtonType
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.LoadingOverlayBox
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.state.ScheduleManagementEvent
import com.onmoim.feature.groups.viewmodel.ScheduleManagementViewModel
import java.time.LocalDateTime

@Composable
fun ScheduleManagementRoute(
    scheduleManagementViewModel: ScheduleManagementViewModel
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedDeleteMeetingId by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val isLoading by scheduleManagementViewModel.isLoading.collectAsStateWithLifecycle()

    if (showDeleteDialog) {
        CommonDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            onClickDismiss = {
                showDeleteDialog = false
            },
            onClickConfirm = {
                showDeleteDialog = false
                scheduleManagementViewModel.deleteMeeting(selectedDeleteMeetingId)
            },
            title = stringResource(R.string.schedule_management_delete_dialog_title),
            content = stringResource(R.string.schedule_management_delete_dialog_content)
        )
    }

    LoadingOverlayBox(
        loading = isLoading
    ) {
        ScheduleManagementScreen(
            onBack = {
                onBackPressedDispatcher?.onBackPressed()
            },
            onClickDelete = { meetingId ->
                selectedDeleteMeetingId = meetingId
                showDeleteDialog = true
            }
        )
    }

    LaunchedEffect(Unit) {
        scheduleManagementViewModel.event.collect { event ->
            when (event) {
                is ScheduleManagementEvent.DeleteFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                ScheduleManagementEvent.DeleteSuccess -> {
                    Toast.makeText(
                        context,
                        R.string.schedule_management_delete_success,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

@Composable
private fun ScheduleManagementScreen(
    onBack: () -> Unit,
    onClickDelete: (meetingId: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.schedule_management),
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
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 15.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(10) {
                ComingScheduleCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClickButton = {

                    },
                    buttonType = ComingScheduleCardButtonType.DELETE,
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

@Preview
@Composable
private fun ScheduleManagementScreenPreview() {
    OnmoimTheme {
        ScheduleManagementScreen(
            onBack = {},
            onClickDelete = {}
        )
    }
}