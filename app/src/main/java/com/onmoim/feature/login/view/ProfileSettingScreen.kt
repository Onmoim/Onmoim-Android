package com.onmoim.feature.login.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.R
import com.onmoim.core.constant.Sex
import com.onmoim.core.ui.text.DateVisualTransformation
import com.onmoim.core.ui.theme.OnmoimTheme
import com.onmoim.core.ui.theme.TopAppBarHeight
import com.onmoim.core.ui.theme.pretendard
import com.onmoim.feature.login.state.ProfileSettingEvent
import com.onmoim.feature.login.state.ProfileSettingState
import com.onmoim.feature.login.viewmodel.ProfileSettingViewModel

@Composable
fun ProfileSettingRoute(
    profileSettingViewModel: ProfileSettingViewModel = hiltViewModel(),
    onNavigateToLocationSetting: () -> Unit,
    onNavigateToSelectInterest: () -> Unit
) {
    val profileSettingState by profileSettingViewModel.profileSettingState.collectAsStateWithLifecycle()
    var showLoading by remember { mutableStateOf(false) }

    ProfileSettingScreen(
        profileSettingState = profileSettingState,
        showLoading = showLoading,
        onNameChanged = profileSettingViewModel::onNameChanged,
        onSexChanged = profileSettingViewModel::onSexChanged,
        onBirthChanged = profileSettingViewModel::onBirthChanged,
        onClickLocation = onNavigateToLocationSetting,
        onClickComplete = profileSettingViewModel::onClickComplete
    )

    LaunchedEffect(Unit) {
        profileSettingViewModel.receiveEvent.collect { event ->
            when (event) {
                ProfileSettingEvent.Loading -> {
                    showLoading = true
                }

                is ProfileSettingEvent.ProfileSettingFailed -> {
                    showLoading = false
                    // TODO: 에러 처리
                }

                ProfileSettingEvent.ProfileSettingSuccess -> {
                    onNavigateToSelectInterest()
                }
            }
        }
    }
}

@Composable
private fun ProfileSettingScreen(
    profileSettingState: ProfileSettingState,
    showLoading: Boolean,
    onNameChanged: (String) -> Unit,
    onSexChanged: (Sex) -> Unit,
    onBirthChanged: (String) -> Unit,
    onClickLocation: () -> Unit,
    onClickComplete: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ProfileSettingTitle()
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
                    ProfileSettingTextField(
                        value = profileSettingState.name,
                        onValueChange = onNameChanged,
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 37.dp),
                        hint = stringResource(R.string.name)
                    )
                    SexToggle(
                        value = profileSettingState.sex,
                        onValueChanged = onSexChanged,
                        modifier = Modifier.width(100.dp)
                    )
                }
                ProfileSettingTextField(
                    value = profileSettingState.birth,
                    onValueChange = onBirthChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 37.dp),
                    hint = stringResource(R.string.birth),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    visualTransformation = DateVisualTransformation()
                )
                ProfileSettingTextField(
                    value = profileSettingState.location,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 37.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickLocation
                        ),
                    enabled = false,
                    hint = stringResource(R.string.location)
                )
            }
            Spacer(Modifier.height(40.dp))
            ProfileSettingCompleteButton(
                onClick = onClickComplete,
                modifier = Modifier
                    .padding(horizontal = 60.dp)
                    .fillMaxWidth()
                    .heightIn(min = 38.dp),
                enabled = profileSettingState.isValidInputValue
            )
        }
        if (showLoading) {
            Box(
                modifier = Modifier
                    .pointerInteropFilter {
                        false
                    }
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun ProfileSettingTitle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopAppBarHeight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.profile_setting_title),
            style = OnmoimTheme.typography.body1SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
    }
}

@Composable
private fun ProfileSettingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hint: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        textStyle = OnmoimTheme.typography.body2Regular.copy(
            color = OnmoimTheme.colors.textColor
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .background(
                    color = OnmoimTheme.colors.gray01,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.5.dp
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            AnimatedVisibility(
                visible = value.isEmpty() && hint != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                hint?.let {
                    Text(
                        text = it,
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                }
            }
            innerTextField()
        }
    }
}

@Composable
private fun SexToggle(
    value: Sex?,
    onValueChanged: (Sex) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .border(
                width = 0.5.dp,
                color = OnmoimTheme.colors.gray04,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Sex.entries.forEach {
            Box(
                modifier = Modifier
                    .clickable {
                        onValueChanged(it)
                    }
                    .weight(1f)
                    .heightIn(min = 38.dp)
                    .padding(
                        start = when (it) {
                            Sex.MALE -> 0.dp
                            Sex.FEMALE -> 15.dp
                        },
                        end = when (it) {
                            Sex.MALE -> 15.dp
                            Sex.FEMALE -> 0.dp
                        }
                    ),
                contentAlignment = when (it) {
                    Sex.MALE -> Alignment.CenterEnd
                    Sex.FEMALE -> Alignment.CenterStart
                }
            ) {
                Text(
                    text = when (it) {
                        Sex.MALE -> stringResource(R.string.male)
                        Sex.FEMALE -> stringResource(R.string.female)
                    },
                    style = OnmoimTheme.typography.body2Regular.copy(
                        color = if (value == it) {
                            OnmoimTheme.colors.textColor
                        } else {
                            OnmoimTheme.colors.gray04
                        }
                    )
                )
            }
        }
    }
}

@Composable
private fun ProfileSettingCompleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .background(
                color = if (enabled) {
                    OnmoimTheme.colors.primaryBlue
                } else {
                    OnmoimTheme.colors.gray05
                },
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                enabled = enabled
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.btn_ok),
            fontFamily = pretendard,
            fontWeight = FontWeight.W600,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = OnmoimTheme.colors.backgroundColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSettingScreenPreview() {
    var profileSettingState by remember {
        mutableStateOf(ProfileSettingState(location = "연남동"))
    }

    OnmoimTheme {
        ProfileSettingScreen(
            profileSettingState = profileSettingState,
            showLoading = false,
            onNameChanged = {
                profileSettingState = profileSettingState.copy(name = it)
            },
            onSexChanged = {
                profileSettingState = profileSettingState.copy(sex = it)
            },
            onBirthChanged = {
                if (it.length <= 8) {
                    profileSettingState = profileSettingState.copy(birth = it)
                }
            },
            onClickLocation = {},
            onClickComplete = {}
        )
    }
}