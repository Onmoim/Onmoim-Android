package com.onmoim.feature.groups.view

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.onmoim.core.designsystem.component.CommonTextField
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.group.CategoryIcon
import com.onmoim.core.designsystem.component.group.GroupImageBox
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.LoadingOverlayBox
import com.onmoim.core.util.FileUtil
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.state.GroupEditEvent
import com.onmoim.feature.groups.state.GroupEditUiState
import com.onmoim.feature.groups.viewmodel.GroupEditViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GroupEditRoute(
    groupEditViewModel: GroupEditViewModel,
    onBackAndRefresh: () -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val uiState by groupEditViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        scope.launch(Dispatchers.IO) {
            FileUtil.createTempImageFile(context, uri)?.let {
                groupEditViewModel.onImageChange(it.absolutePath)
            }
        }
    }

    LoadingOverlayBox(
        loading = uiState.isLoading
    ) {
        GroupEditScreen(
            onBack = {
                onBackPressedDispatcher?.onBackPressed()
            },
            onClickImage = {
                pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onGroupDescriptionChange = groupEditViewModel::onGroupDescriptionChange,
            onGroupCapacityChange = groupEditViewModel::onGroupCapacityChange,
            onClickConfirm = groupEditViewModel::updateGroupInfo,
            uiState = uiState
        )
    }

    LaunchedEffect(Unit) {
        groupEditViewModel.event.collect { event ->
            when (event) {
                is GroupEditEvent.EditFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                GroupEditEvent.EditSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.group_edit_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackAndRefresh()
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            scope.launch(Dispatchers.IO) {
                FileUtil.removeTempImagesDir(context)
            }
        }
    }
}

@Composable
private fun GroupEditScreen(
    onBack: () -> Unit,
    onClickImage: () -> Unit,
    onGroupDescriptionChange: (String) -> Unit,
    onGroupCapacityChange: (Int?) -> Unit,
    onClickConfirm: () -> Unit,
    uiState: GroupEditUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
            .imePadding()
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.group_edit),
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
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            GroupImageBox(
                onClick = onClickImage,
                modifier = Modifier.aspectRatio(20 / 9f),
                imageUrl = uiState.newGroupImageUrl
            )
            Text(
                text = stringResource(R.string.group_category),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            Row(
                modifier = Modifier.padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CommonTextField(
                    value = uiState.categoryName,
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    enabled = false,
                    textColor = OnmoimTheme.colors.gray05
                )
                CategoryIcon(
                    modifier = Modifier.padding(start = 10.dp),
                    imageUrl = uiState.categoryImageUrl
                )
            }
            Text(
                text = stringResource(R.string.group_edit_location),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            CommonTextField(
                value = uiState.locationName,
                onValueChange = {},
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                enabled = false,
                textColor = OnmoimTheme.colors.gray05
            )
            Text(
                text = stringResource(R.string.group_description),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            CommonTextField(
                value = uiState.groupName,
                onValueChange = {},
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                enabled = false,
                textColor = OnmoimTheme.colors.gray05
            )
            Spacer(Modifier.height(16.dp))
            CommonTextField(
                value = uiState.newGroupDescription,
                onValueChange = onGroupDescriptionChange,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.group_open_group_description_hint),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                },
                singleLine = false,
                innerFieldAlignment = Alignment.TopStart
            )
            Text(
                text = stringResource(R.string.group_capacity, 5, 300),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            CommonTextField(
                value = uiState.newGroupCapacity?.toString() ?: "",
                onValueChange = { value ->
                    onGroupCapacityChange(value.toIntOrNull())
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.group_open_group_capacity_hint),
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
                enabled = uiState.isValid()
            )
        }
    }
}

@Preview
@Composable
private fun GroupEditScreenPreview() {
    OnmoimTheme {
        GroupEditScreen(
            onBack = {},
            onClickImage = {},
            onGroupDescriptionChange = {},
            onGroupCapacityChange = {},
            onClickConfirm = {},
            uiState = GroupEditUiState(
                locationName = "지역",
                groupName = "이름",
                groupDescription = "설명",
                categoryName = "카테고리",
                groupCapacity = 20
            )
        )
    }
}