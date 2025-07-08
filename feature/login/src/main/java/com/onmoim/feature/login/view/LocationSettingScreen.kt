package com.onmoim.feature.login.view

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonTextField
import com.onmoim.core.designsystem.component.ErrorAndRetryBox
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.R
import com.onmoim.feature.login.state.LocationSettingUiState
import com.onmoim.feature.login.viewmodel.LocationSettingViewModel

@Composable
fun LocationSettingRoute(
    locationSettingViewModel: LocationSettingViewModel = hiltViewModel(),
    onBack: (address: String?, addressId: Int?) -> Unit
) {
    val uiState by locationSettingViewModel.uiState.collectAsStateWithLifecycle()
    val searchKeyword by locationSettingViewModel.searchKeyword.collectAsStateWithLifecycle()

    BackHandler {
        onBack(null, null)
    }

    LocationSettingScreen(
        onBack = onBack,
        uiState = uiState,
        searchKeyword = searchKeyword,
        onSearchKeywordChange = locationSettingViewModel::onSearchKeywordChange,
        onSearch = {
            locationSettingViewModel.fetchLocationSearch(searchKeyword)
        },
        onClickRefresh = {
            locationSettingViewModel.fetchLocationSearch(searchKeyword)
        }
    )
}

@Composable
private fun LocationSettingScreen(
    onBack: (address: String?, addressId: Int?) -> Unit,
    uiState: LocationSettingUiState,
    searchKeyword: String,
    onSearchKeywordChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClickRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(
                color = OnmoimTheme.colors.backgroundColor
            )
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.location_setting_title),
                    style = OnmoimTheme.typography.body1SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
            },
            navigationIcon = {
                NavigationIconButton(
                    onClick = {
                        onBack(null, null)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        )
        Spacer(Modifier.height(8.dp))
        CommonTextField(
            value = searchKeyword,
            onValueChange = onSearchKeywordChange,
            modifier = Modifier.padding(horizontal = 15.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.find_location_placeholder),
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
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                }
            )
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is LocationSettingUiState.Error -> {
                    ErrorAndRetryBox(
                        onClickRefresh = onClickRefresh,
                        modifier = Modifier.align(Alignment.Center),
                        title = stringResource(R.string.temporary_error),
                        content = stringResource(R.string.try_again_later)
                    )
                }

                is LocationSettingUiState.Result -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.locations) {
                            LocationItem(
                                onClick = {
                                    onBack(it.dong, it.id)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                label = it.dong,
                                subLabel = "${it.city} ${it.district}"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    subLabel: String
) {
    val dividerColor = OnmoimTheme.colors.gray04

    Row(
        modifier = modifier
            .drawBehind {
                drawLine(
                    color = dividerColor,
                    strokeWidth = 0.5.dp.toPx(),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height)
                )
            }
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = OnmoimTheme.typography.body2Regular.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "($subLabel)",
            style = OnmoimTheme.typography.caption2Regular.copy(
                color = OnmoimTheme.colors.gray05
            )
        )
    }
}

@Preview
@Composable
private fun LocationSettingScreenPreview() {
    var searchKeyword by remember { mutableStateOf("") }

    OnmoimTheme {
        LocationSettingScreen(
            onBack = { _, _ -> },
            uiState = LocationSettingUiState.Result(emptyList()),
            searchKeyword = searchKeyword,
            onSearchKeywordChange = {
                searchKeyword = it
            },
            onSearch = {},
            onClickRefresh = {}
        )
    }
}