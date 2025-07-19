package com.onmoim.feature.login.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.data.model.Category
import com.onmoim.core.designsystem.component.CategoryItem
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.ErrorAndRetryBox
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.R
import com.onmoim.feature.login.state.InterestSelectEvent
import com.onmoim.feature.login.state.InterestSelectUiState
import com.onmoim.feature.login.viewmodel.InterestSelectViewModel

@Composable
fun InterestSelectRoute(
    interestSelectViewModel: InterestSelectViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val uiState by interestSelectViewModel.uiState.collectAsStateWithLifecycle()
    val selectedInterestIds by interestSelectViewModel.selectedInterestIds.collectAsStateWithLifecycle()
    var showLoading by remember { mutableStateOf(false) }

    BackHandler {
        onBack()
    }

    InterestSelectScreen(
        onBack = onBack,
        onClickRefresh = interestSelectViewModel::fetchInterests,
        showLoading = showLoading,
        uiState = uiState,
        selectedInterestIds = selectedInterestIds,
        onClickCategory = interestSelectViewModel::onClickCategory,
        onClickConfirm = interestSelectViewModel::onClickConfirm
    )

    LaunchedEffect(Unit) {
        interestSelectViewModel.event.collect { event ->
            when (event) {
                is InterestSelectEvent.ShowErrorDialog -> {
                    showLoading = false
                    // TODO: 에러 처리
                }

                InterestSelectEvent.Loading -> {
                    showLoading = true
                }

                InterestSelectEvent.NavigateToHome -> onNavigateToHome()
            }
        }
    }
}

@Composable
private fun InterestSelectScreen(
    onBack: () -> Unit,
    onClickRefresh: () -> Unit,
    showLoading: Boolean,
    uiState: InterestSelectUiState,
    selectedInterestIds: Set<Int>,
    onClickCategory: (id: Int) -> Unit,
    onClickConfirm: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(OnmoimTheme.colors.backgroundColor)
            .fillMaxSize()
    ) {
        Column {
            CommonAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.interest_select_title),
                        style = OnmoimTheme.typography.body1SemiBold.copy(
                            color = OnmoimTheme.colors.textColor
                        )
                    )
                },
                navigationIcon = {
                    NavigationIconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    if (selectedInterestIds.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.confirm),
                            modifier = Modifier
                                .clickable(
                                    onClick = onClickConfirm,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                )
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 14.dp
                                ),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.textColor
                            ),
                        )
                    }
                }
            )
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (uiState) {
                    is InterestSelectUiState.Error -> {
                        ErrorAndRetryBox(
                            onClickRefresh = onClickRefresh,
                            modifier = Modifier.align(Alignment.Center),
                            title = stringResource(R.string.temporary_error),
                            content = stringResource(R.string.try_again_later)
                        )
                    }

                    InterestSelectUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is InterestSelectUiState.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(
                                start = 43.dp,
                                end = 43.dp,
                                bottom = 20.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.categories) {
                                CategoryItem(
                                    onClick = {
                                        onClickCategory(it.id)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    selected = selectedInterestIds.contains(it.id),
                                    imageUrl = it.imageUrl,
                                    label = it.name
                                )
                            }
                        }
                    }
                }
            }
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
private fun InterestSelectScreenPreview() {
    var selectedInterestIds by remember {
        mutableStateOf(setOf(0))
    }

    OnmoimTheme {
        InterestSelectScreen(
            onBack = {},
            onClickRefresh = {},
            showLoading = false,
            uiState = InterestSelectUiState.Success(
                List(20) {
                    Category(
                        id = it,
                        imageUrl = null,
                        name = "카테고리 $it"
                    )
                }
            ),
            selectedInterestIds = selectedInterestIds,
            onClickCategory = {
                val copySet = selectedInterestIds.toMutableSet()
                if (copySet.contains(it)) {
                    copySet.remove(it)
                } else {
                    copySet.add(it)
                }
                selectedInterestIds = copySet
            },
            onClickConfirm = {}
        )
    }
}