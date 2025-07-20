package com.onmoim.feature.login.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonButton
import com.onmoim.core.designsystem.component.CommonDatePickerDialog
import com.onmoim.core.designsystem.component.CommonDialog
import com.onmoim.core.designsystem.component.CommonTextField
import com.onmoim.core.designsystem.component.GenderToggle
import com.onmoim.core.designsystem.constant.Gender
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.R
import com.onmoim.feature.login.state.ProfileSettingEvent
import com.onmoim.feature.login.state.ProfileSettingState
import com.onmoim.feature.login.viewmodel.ProfileSettingViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProfileSettingRoute(
    profileSettingViewModel: ProfileSettingViewModel,
    onNavigateToLocationSetting: () -> Unit,
    onNavigateToInterestSelect: () -> Unit
) {
    val profileSettingState by profileSettingViewModel.profileSettingState.collectAsStateWithLifecycle()
    var showLoading by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (showDatePicker) {
        CommonDatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            initialDate = profileSettingState.birth ?: LocalDate.now(),
            onClickConfirm = { localDate ->
                showDatePicker = false
                profileSettingViewModel.onBirthChange(localDate)
            }
        )
    }

    if (showErrorDialog) {
        CommonDialog(
            onDismissRequest = {
                showErrorDialog = false
            },
            content = errorMessage,
            onClickConfirm = {
                showErrorDialog = false
            }
        )
    }

    ProfileSettingScreen(
        profileSettingState = profileSettingState,
        showLoading = showLoading,
        onNameChange = profileSettingViewModel::onNameChange,
        onGenderChange = profileSettingViewModel::onGenderChange,
        onClickBirth = {
            showDatePicker = true
        },
        onClickLocation = onNavigateToLocationSetting,
        onClickComplete = profileSettingViewModel::onClickConfirm
    )

    LaunchedEffect(Unit) {
        profileSettingViewModel.event.collect { event ->
            when (event) {
                ProfileSettingEvent.Loading -> {
                    showLoading = true
                }

                is ProfileSettingEvent.ProfileSettingFailed -> {
                    showLoading = false
                    errorMessage = ContextCompat.getString(context, R.string.profile_setting_error)
                    showErrorDialog = true
                }

                ProfileSettingEvent.ProfileSettingSuccess -> {
                    onNavigateToInterestSelect()
                }
            }
        }
    }
}

@Composable
private fun ProfileSettingScreen(
    profileSettingState: ProfileSettingState,
    showLoading: Boolean,
    onNameChange: (String) -> Unit,
    onGenderChange: (Gender) -> Unit,
    onClickBirth: () -> Unit,
    onClickLocation: () -> Unit,
    onClickComplete: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(OnmoimTheme.colors.backgroundColor)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_setting_title),
                        style = OnmoimTheme.typography.body1SemiBold.copy(
                            color = OnmoimTheme.colors.textColor
                        )
                    )
                }
            )
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CommonTextField(
                        value = profileSettingState.name,
                        onValueChange = onNameChange,
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.name),
                                style = OnmoimTheme.typography.body2Regular.copy(
                                    color = OnmoimTheme.colors.gray04
                                )
                            )
                        }
                    )
                    GenderToggle(
                        value = profileSettingState.gender,
                        onValueChange = onGenderChange,
                        modifier = Modifier.width(100.dp)
                    )
                }
                AnimatedVisibility(
                    visible = profileSettingState.name.isNotBlank() &&
                            !profileSettingState.isValidKoreanNameFormat()
                ) {
                    Text(
                        text = stringResource(R.string.error_name_format),
                        style = OnmoimTheme.typography.caption2Regular.copy(
                            color = OnmoimTheme.colors.alertRed
                        )
                    )
                }
                CommonTextField(
                    value = profileSettingState.birth?.let {
                        DateTimeFormatter.ofPattern("yyyyMMdd").format(it)
                    } ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickBirth
                        ),
                    enabled = false,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.birth),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.gray04
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                CommonTextField(
                    value = profileSettingState.location,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickLocation
                        ),
                    enabled = false,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.location),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.gray04
                            )
                        )
                    }
                )
            }
            Spacer(Modifier.height(40.dp))
            CommonButton(
                onClick = onClickComplete,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = profileSettingState.isValidInputValue
            )
        }
        if (showLoading) {
            Box(
                modifier = Modifier
                    .pointerInteropFilter {
                        true
                    }
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun ProfileSettingScreenPreview() {
    var profileSettingState by remember {
        mutableStateOf(ProfileSettingState(location = "연남동"))
    }

    OnmoimTheme {
        ProfileSettingScreen(
            profileSettingState = profileSettingState,
            showLoading = false,
            onNameChange = {
                profileSettingState = profileSettingState.copy(name = it)
            },
            onGenderChange = {
                profileSettingState = profileSettingState.copy(gender = it)
            },
            onClickBirth = {},
            onClickLocation = {},
            onClickComplete = {}
        )
    }
}