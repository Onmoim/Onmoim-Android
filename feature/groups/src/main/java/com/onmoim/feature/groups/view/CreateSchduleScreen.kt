package com.onmoim.feature.groups.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonButton
import com.onmoim.core.designsystem.component.CommonDatePickerDialog
import com.onmoim.core.designsystem.component.CommonTextField
import com.onmoim.core.designsystem.component.CommonTimePickerDialog
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.meeting.ScheduleImageRegBox
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.util.FileUtil
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.GroupMemberRole
import com.onmoim.feature.groups.constant.ScheduleType
import com.onmoim.feature.groups.state.CreateScheduleUiState
import com.onmoim.feature.groups.viewmodel.CreateScheduleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CreateScheduleRoute(
    createScheduleViewModel: CreateScheduleViewModel,
    groupMemberRole: GroupMemberRole,
    onNavigateToMeetingLocationSearch: () -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val uiState by createScheduleViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        scope.launch(Dispatchers.IO) {
            FileUtil.createTempImageFile(context, uri)?.let {
                createScheduleViewModel.onImageChange(it.absolutePath)
            }
        }
    }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        CommonDatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            initialDate = uiState.startDate ?: LocalDate.now(),
            minDate = LocalDate.now(),
            maxDate = LocalDate.of(9999, 12, 31),
            onClickConfirm = { localDate ->
                showDatePicker = false
                createScheduleViewModel.onStartDateChange(localDate)
            }
        )
    }

    if (showTimePicker) {
        CommonTimePickerDialog(
            onDismissRequest = {
                showTimePicker = false
            },
            initialTime = uiState.startTime ?: if (uiState.startDate == LocalDate.now()) {
                LocalTime.now()
            } else {
                LocalTime.MIN
            },
            minTime = if (uiState.startDate == LocalDate.now()) {
                LocalTime.now()
            } else {
                LocalTime.MIN
            },
            onClickConfirm = { localTime ->
                showTimePicker = false
                createScheduleViewModel.onStartTimeChange(localTime)
            }
        )
    }

    CreateScheduleScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        groupMemberRole = groupMemberRole,
        uiState = uiState,
        onClickImage = {
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onScheduleTypeChange = createScheduleViewModel::onScheduleTypeChange,
        onScheduleNameChange = createScheduleViewModel::onScheduleNameChange,
        onClickDateSelect = {
            showDatePicker = true
        },
        onClickTimeSelect = {
            showTimePicker = true
        },
        onClickPlaceSelect = onNavigateToMeetingLocationSearch,
        onCostChange = createScheduleViewModel::onCostChange,
        onCapacityChange = createScheduleViewModel::onCapacityChange,
        onClickConfirm = createScheduleViewModel::createSchedule
    )

    DisposableEffect(Unit) {
        onDispose {
            scope.launch(Dispatchers.IO) {
                FileUtil.removeTempImagesDir(context)
            }
        }
    }
}

@Composable
private fun CreateScheduleScreen(
    onBack: () -> Unit,
    groupMemberRole: GroupMemberRole,
    uiState: CreateScheduleUiState,
    onClickImage: () -> Unit,
    onScheduleTypeChange: (ScheduleType) -> Unit,
    onScheduleNameChange: (String) -> Unit,
    onClickDateSelect: () -> Unit,
    onClickTimeSelect: () -> Unit,
    onClickPlaceSelect: () -> Unit,
    onCostChange: (Long?) -> Unit,
    onCapacityChange: (Int?) -> Unit,
    onClickConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(OnmoimTheme.colors.backgroundColor)
            .fillMaxSize()
            .imePadding()
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.create_schedule),
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
                .verticalScroll(rememberScrollState())
        ) {
            ScheduleImageRegBox(
                onClick = onClickImage,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp)
                    .size(240.dp, 150.dp),
                imagePath = uiState.imagePath
            )
            if (groupMemberRole == GroupMemberRole.OWNER) {
                Text(
                    text = stringResource(R.string.create_schedule_meeting_select),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    style = OnmoimTheme.typography.body2SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                ScheduleTypeToggle(
                    onClick = onScheduleTypeChange,
                    modifier = Modifier.padding(horizontal = 15.dp),
                    selectedType = uiState.type
                )
            }
            Text(
                text = stringResource(R.string.create_schedule_schedule_name),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            CommonTextField(
                value = uiState.name,
                onValueChange = onScheduleNameChange,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.create_schedule_schedule_name_hint),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                }
            )
            Text(
                text = stringResource(R.string.create_schedule_date_and_time),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                CommonTextField(
                    value = uiState.startDate?.let {
                        DateTimeFormatter.ofPattern("yyyy. MM. dd").format(it)
                    } ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickDateSelect
                        )
                        .weight(1f),
                    enabled = false,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.create_schedule_date_select),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.gray04
                            )
                        )
                    }
                )
                CommonTextField(
                    value = uiState.startTime?.let {
                        DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN).format(it)
                    } ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickTimeSelect
                        )
                        .weight(1f),
                    enabled = false,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.create_schedule_time_select),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.gray04
                            )
                        )
                    }
                )
            }
            Text(
                text = stringResource(R.string.create_schedule_location),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            CommonTextField(
                value = uiState.place,
                onValueChange = {},
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onClickPlaceSelect
                    ),
                enabled = false,
                placeholder = {
                    Text(
                        text = stringResource(R.string.create_schedule_meeting_location_select),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = OnmoimTheme.colors.gray04
                    )
                }
            )
            Text(
                text = stringResource(R.string.create_schedule_cost),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            CommonTextField(
                value = uiState.cost?.toString() ?: "",
                onValueChange = { value ->
                    onCostChange(value.toLongOrNull())
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.create_schedule_cost_hint),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(
                text = stringResource(R.string.create_schedule_capacity),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            CommonTextField(
                value = uiState.capacity?.toString() ?: "",
                onValueChange = { value ->
                    onCapacityChange(value.toIntOrNull())
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.create_schedule_capacity_hint),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            CommonButton(
                onClick = onClickConfirm,
                modifier = Modifier
                    .padding(vertical = 40.dp)
                    .align(Alignment.CenterHorizontally),
                enabled = uiState.isValid(groupMemberRole)
            )
        }
    }
}

@Composable
private fun ScheduleTypeToggle(
    onClick: (ScheduleType) -> Unit,
    modifier: Modifier = Modifier,
    selectedType: ScheduleType?
) {
    Row(
        modifier = modifier
            .border(
                width = 0.5.dp,
                color = OnmoimTheme.colors.gray04,
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onClick(ScheduleType.REGULAR)
                    }
                )
                .weight(1f)
                .height(38.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.regular_meet),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = if (selectedType == ScheduleType.REGULAR) {
                        OnmoimTheme.colors.textColor
                    } else {
                        OnmoimTheme.colors.gray04
                    }
                )
            )
        }
        VerticalDivider(
            modifier = Modifier.padding(vertical = 7.dp),
            thickness = 0.5.dp,
            color = OnmoimTheme.colors.gray04
        )
        Box(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onClick(ScheduleType.LIGHTNING)
                    }
                )
                .weight(1f)
                .height(38.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.lightning),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = if (selectedType == ScheduleType.LIGHTNING) {
                        OnmoimTheme.colors.textColor
                    } else {
                        OnmoimTheme.colors.gray04
                    }
                )
            )
        }
    }
}

@Preview
@Composable
private fun CreateScheduleScreenForOwnerPreview() {
    OnmoimTheme {
        CreateScheduleScreen(
            onBack = {},
            groupMemberRole = GroupMemberRole.OWNER,
            uiState = CreateScheduleUiState(),
            onClickImage = {},
            onScheduleTypeChange = {},
            onScheduleNameChange = {},
            onClickDateSelect = {},
            onClickTimeSelect = {},
            onClickPlaceSelect = {},
            onCostChange = {},
            onCapacityChange = {},
            onClickConfirm = {}
        )
    }
}

@Preview
@Composable
private fun CreateScheduleScreenForMemberPreview() {
    OnmoimTheme {
        CreateScheduleScreen(
            onBack = {},
            groupMemberRole = GroupMemberRole.MEMBER,
            uiState = CreateScheduleUiState(),
            onClickImage = {},
            onScheduleTypeChange = {},
            onScheduleNameChange = {},
            onClickDateSelect = {},
            onClickTimeSelect = {},
            onClickPlaceSelect = {},
            onCostChange = {},
            onCapacityChange = {},
            onClickConfirm = {}
        )
    }
}