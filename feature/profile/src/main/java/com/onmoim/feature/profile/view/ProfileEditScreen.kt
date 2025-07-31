package com.onmoim.feature.profile.view

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonChip
import com.onmoim.core.designsystem.component.CommonDatePickerDialog
import com.onmoim.core.designsystem.component.CommonTextField
import com.onmoim.core.designsystem.component.GenderToggle
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.constant.Gender
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.designsystem.theme.shadow1
import com.onmoim.core.ui.LoadingOverlayBox
import com.onmoim.core.ui.shimmerBackground
import com.onmoim.core.util.FileUtil
import com.onmoim.feature.profile.R
import com.onmoim.feature.profile.state.ProfileEditEvent
import com.onmoim.feature.profile.state.ProfileEditUiState
import com.onmoim.feature.profile.viewmodel.ProfileEditViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProfileEditRoute(
    profileEditViewModel: ProfileEditViewModel,
    onBackAndRefresh: () -> Unit,
    onNavigateToLocationSearch: () -> Unit
) {
    val uiState by profileEditViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by profileEditViewModel.isLoadingState.collectAsStateWithLifecycle()
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        scope.launch(Dispatchers.IO) {
            FileUtil.createTempImageFile(context, uri)?.let {
                profileEditViewModel.onProfileImageChange(it.absolutePath)
            }
        }
    }

    if (showDatePicker) {
        CommonDatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            initialDate = uiState.birth ?: LocalDate.now(),
            onClickConfirm = { localDate ->
                showDatePicker = false
                profileEditViewModel.onBirthChange(localDate)
            }
        )
    }

    LoadingOverlayBox(
        loading = isLoading,
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileEditScreen(
            onBack = {
                onBackPressedDispatcher?.onBackPressed()
            },
            onClickComplete = profileEditViewModel::updateProfile,
            onClickCamera = {
                pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onClickBirthField = {
                showDatePicker = true
            },
            onClickLocationField = onNavigateToLocationSearch,
            onClickInterestEdit = {},
            uiState = uiState,
            onNameChange = profileEditViewModel::onNameChange,
            onGenderChange = profileEditViewModel::onGenderChange,
            onIntroductionChange = profileEditViewModel::onIntroductionChange
        )
    }

    LaunchedEffect(Unit) {
        profileEditViewModel.event.collect { event ->
            when (event) {
                is ProfileEditEvent.UpdateFailure -> {
                    Toast.makeText(context, event.t.message.toString(), Toast.LENGTH_SHORT).show()
                }

                ProfileEditEvent.UpdateSuccess -> {
                    scope.launch(Dispatchers.IO) {
                        FileUtil.removeTempImagesDir(context)
                    }
                    onBackAndRefresh()
                }
            }
        }
    }
}

@Composable
private fun ProfileEditScreen(
    onBack: () -> Unit,
    onClickComplete: () -> Unit,
    onClickCamera: () -> Unit,
    onClickBirthField: () -> Unit,
    onClickLocationField: () -> Unit,
    onClickInterestEdit: () -> Unit,
    uiState: ProfileEditUiState,
    onNameChange: (String) -> Unit,
    onGenderChange: (Gender) -> Unit,
    onIntroductionChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.profile_edit),
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
                Text(
                    text = stringResource(R.string.complete),
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickComplete
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    style = OnmoimTheme.typography.body2Regular.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
            }
        )
        ProfileImage(
            onClickCamera = onClickCamera,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .align(Alignment.CenterHorizontally),
            imageUrl = uiState.newImagePath ?: uiState.originImageUrl
        )
        Text(
            text = stringResource(R.string.profile_edit_name),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
            style = OnmoimTheme.typography.body2SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Row(
            modifier = Modifier.padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = stringResource(R.string.profile_edit_name),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                }
            )
            GenderToggle(
                value = uiState.gender,
                onValueChange = onGenderChange,
                modifier = Modifier.width(100.dp)
            )
        }
        AnimatedVisibility(
            visible = !uiState.isValidKoreanNameFormat(),
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.profile_edit_error_name_format),
                style = OnmoimTheme.typography.caption2Regular.copy(
                    color = OnmoimTheme.colors.alertRed
                )
            )
        }
        Text(
            text = stringResource(R.string.profile_edit_birth),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
            style = OnmoimTheme.typography.body2SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        CommonTextField(
            value = uiState.birth?.let {
                DateTimeFormatter.ofPattern("yyyyMMdd").format(it)
            } ?: "",
            onValueChange = {},
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickBirthField
                ),
            enabled = false,
            placeholder = {
                Text(
                    text = stringResource(R.string.profile_edit_birth),
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
            text = stringResource(R.string.profile_edit_location),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
            style = OnmoimTheme.typography.body2SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        CommonTextField(
            value = uiState.locationName,
            onValueChange = {},
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickLocationField
                ),
            enabled = false,
            placeholder = {
                Text(
                    text = stringResource(R.string.profile_edit_location),
                    style = OnmoimTheme.typography.body2Regular.copy(
                        color = OnmoimTheme.colors.gray04
                    )
                )
            }
        )
        Text(
            text = stringResource(R.string.profile_edit_intro),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
            style = OnmoimTheme.typography.body2SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        CommonTextField(
            value = uiState.introduction,
            onValueChange = onIntroductionChange,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .height(100.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.profile_edit_intro),
                    style = OnmoimTheme.typography.body2Regular.copy(
                        color = OnmoimTheme.colors.gray04
                    )
                )
            },
            singleLine = false,
            innerFieldAlignment = Alignment.TopStart
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.profile_edit_interest),
                modifier = Modifier.padding(start = 16.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            Text(
                text = stringResource(R.string.edit),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onClickInterestEdit
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.caption1SemiBold.copy(
                    color = OnmoimTheme.colors.primaryBlue
                )
            )
        }
        Spacer(Modifier.height(4.dp))
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.categories.forEach {
                CommonChip(
                    label = it.second,
                    textColor = OnmoimTheme.colors.gray05,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileImage(
    onClickCamera: () -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String?
) {
    val profilePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(imageUrl)
        }.build()
    )
    val profilePainterState by profilePainter.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
    ) {
        Crossfade(
            targetState = profilePainterState,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        ) { state ->
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmerBackground()
                    )
                }

                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = state.painter,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Image(
                        painter = painterResource(R.drawable.ic_user_80),
                        contentDescription = null
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 4.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickCamera
                )
                .size(32.dp)
                .shadow1(999.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun ProfileEditScreenPreview() {
    OnmoimTheme {
        ProfileEditScreen(
            onBack = {},
            onClickComplete = {},
            onClickCamera = {},
            onClickBirthField = {},
            onClickLocationField = {},
            onClickInterestEdit = {},
            uiState = ProfileEditUiState(),
            onNameChange = {},
            onGenderChange = {},
            onIntroductionChange = {}
        )
    }
}