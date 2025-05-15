package com.onmoim.feature.login.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.R
import com.onmoim.core.constant.InterestCategory
import com.onmoim.core.ui.component.CommonAppBar
import com.onmoim.core.ui.component.NavigationIconButton
import com.onmoim.core.ui.theme.OnmoimTheme
import com.onmoim.core.ui.theme.shadow1
import com.onmoim.feature.login.state.InterestSelectEvent
import com.onmoim.feature.login.viewmodel.InterestSelectViewModel

@Composable
fun InterestSelectRoute(
    interestSelectViewModel: InterestSelectViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val selectedInterestCategories by interestSelectViewModel.selectedInterestCategories.collectAsStateWithLifecycle()
    var showLoading by remember { mutableStateOf(false) }

    BackHandler {
        onBack()
    }

    InterestSelectScreen(
        onBack = onBack,
        showLoading = showLoading,
        selectedInterestCategories = selectedInterestCategories,
        onClickCategory = interestSelectViewModel::onClickCategory,
        onClickOk = interestSelectViewModel::onClickOk
    )

    LaunchedEffect(Unit) {
        interestSelectViewModel.receiveEvent.collect { event ->
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
    showLoading: Boolean,
    selectedInterestCategories: Set<InterestCategory>,
    onClickCategory: (InterestCategory) -> Unit,
    onClickOk: () -> Unit
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
                    if (selectedInterestCategories.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.confirm),
                            modifier = Modifier
                                .clickable(
                                    onClick = onClickOk,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                )
                                .padding(16.dp),
                            style = OnmoimTheme.typography.body2Regular.copy(
                                color = OnmoimTheme.colors.textColor
                            ),
                        )
                    }
                }
            )
            Spacer(Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 43.dp,
                    end = 43.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                items(InterestCategory.entries) {
                    InterestItem(
                        onClick = {
                            onClickCategory(it)
                        },
                        selected = selectedInterestCategories.contains(it),
                        painter = painterResource(it.iconId),
                        label = stringResource(it.labelId)
                    )
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

@Composable
private fun InterestItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean,
    painter: Painter,
    label: String
) {
    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .shadow1(999.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = label,
                modifier = Modifier.size(50.dp)
            )
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(
                            color = Color(0xFF565656).copy(alpha = 0.8f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            style = OnmoimTheme.typography.caption2Regular.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
    }
}

@Preview
@Composable
private fun InterestSelectScreenPreview() {
    var selectedInterestCategories by remember {
        mutableStateOf(setOf(InterestCategory.EXERCISE))
    }

    OnmoimTheme {
        InterestSelectScreen(
            onBack = {},
            showLoading = false,
            selectedInterestCategories = selectedInterestCategories,
            onClickCategory = {
                val copySet = selectedInterestCategories.toMutableSet()
                if (copySet.contains(it)) {
                    copySet.remove(it)
                } else {
                    copySet.add(it)
                }
                selectedInterestCategories = copySet
            },
            onClickOk = {}
        )
    }
}