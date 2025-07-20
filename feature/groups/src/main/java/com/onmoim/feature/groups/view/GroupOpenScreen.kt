package com.onmoim.feature.groups.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.onmoim.core.designsystem.component.CommonButton
import com.onmoim.core.designsystem.component.CommonTextField
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.designsystem.theme.shadow1
import com.onmoim.core.ui.shimmerBackground
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.state.GroupOpenUiState
import com.onmoim.feature.groups.viewmodel.GroupOpenViewModel

@Composable
fun GroupOpenRoute(
    groupOpenViewModel: GroupOpenViewModel,
    categoryName: String,
    categoryImageUrl: String?,
    onNavigateToLocationSearch: () -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val uiState by groupOpenViewModel.uiState.collectAsStateWithLifecycle()

    GroupOpenScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        categoryName = categoryName,
        categoryImageUrl = categoryImageUrl,
        onClickLocation = onNavigateToLocationSearch,
        onGroupNameChange = groupOpenViewModel::onGroupNameChange,
        onGroupDescriptionChange = groupOpenViewModel::onGroupDescriptionChange,
        onGroupCapacityChange = groupOpenViewModel::onGroupCapacityChange,
        onClickConfirm = groupOpenViewModel::onClickConfirm,
        uiState = uiState
    )
}

@Composable
private fun GroupOpenScreen(
    onBack: () -> Unit,
    categoryName: String,
    categoryImageUrl: String?,
    onClickLocation: () -> Unit,
    onGroupNameChange: (String) -> Unit,
    onGroupDescriptionChange: (String) -> Unit,
    onGroupCapacityChange: (Int?) -> Unit,
    onClickConfirm: () -> Unit,
    uiState: GroupOpenUiState
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
                    text = stringResource(R.string.group_open),
                    style = OnmoimTheme.typography.body1SemiBold
                )
            },
            modifier = Modifier.background(Color.White),
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
            Text(
                text = stringResource(R.string.group_open_category),
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
                    value = categoryName,
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    enabled = false
                )
                CategoryIcon(
                    modifier = Modifier.padding(start = 10.dp),
                    imageUrl = categoryImageUrl
                )
            }
            Text(
                text = stringResource(R.string.group_open_location_setting),
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
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onClickLocation
                    ),
                enabled = false,
                placeholder = {
                    Text(
                        text = stringResource(R.string.group_open_find_location_hint),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                }
            )
            Text(
                text = stringResource(R.string.group_open_group_description),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            CommonTextField(
                value = uiState.groupName,
                onValueChange = onGroupNameChange,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.group_open_group_name),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                }
            )
            Spacer(Modifier.height(16.dp))
            CommonTextField(
                value = uiState.groupDescription,
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
                text = stringResource(R.string.group_open_group_capacity, 0, 300),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            CommonTextField(
                value = uiState.groupCapacity?.toString() ?: "",
                onValueChange = { value ->
                    val capacity = value.toIntOrNull()
                    if (capacity == null || capacity in 0..300) {
                        onGroupCapacityChange(capacity)
                    }
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

@Composable
private fun CategoryIcon(
    modifier: Modifier = Modifier,
    imageUrl: String?
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(imageUrl)
        }.build()
    )
    val painterState by painter.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .size(41.dp)
            .shadow1(999.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = painterState,
            modifier = Modifier.size(29.dp)
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
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {}
            }
        }
    }
}

@Preview
@Composable
private fun GroupOpenScreenPreview() {
    OnmoimTheme {
        GroupOpenScreen(
            onBack = {},
            categoryName = "카테고리 이름",
            categoryImageUrl = "",
            onClickLocation = {},
            onGroupNameChange = {},
            onGroupDescriptionChange = {},
            onGroupCapacityChange = {},
            onClickConfirm = {},
            uiState = GroupOpenUiState()
        )
    }
}