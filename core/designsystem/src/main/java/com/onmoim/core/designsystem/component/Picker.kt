package com.onmoim.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.theme.OnmoimTheme
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun Picker(
    items: List<String>,
    initialValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pickerItems = remember(items) {
        mutableStateListOf(*buildList {
            add("")
            add("")
            addAll(items)
            add("")
            add("")
        }.toTypedArray())
    }
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = pickerItems.indexOf(initialValue).coerceAtLeast(2) - 2
    )
    val currentVisibleItemsInfo by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo }
    }
    var visibleItemsInfoList by remember { mutableStateOf<List<LazyListItemInfo>>(emptyList()) }
    var centerItemIndex by remember { mutableIntStateOf(pickerItems.indexOf(initialValue)) }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(currentVisibleItemsInfo) {
        visibleItemsInfoList = currentVisibleItemsInfo
        if (currentVisibleItemsInfo.size == 5) {
            centerItemIndex = currentVisibleItemsInfo[2].index
            onValueChange(pickerItems[centerItemIndex])
        } else if (currentVisibleItemsInfo.isNotEmpty()) {
            val middleVisibleIndex = currentVisibleItemsInfo.size / 2
            centerItemIndex = currentVisibleItemsInfo[middleVisibleIndex].index
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
    }

    Box(
        modifier = Modifier.size(88.dp, 134.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(88.dp)
                .heightIn(min = 26.dp)
                .background(
                    color = OnmoimTheme.colors.gray03,
                    shape = RoundedCornerShape(5.dp)
                )
                .clip(RoundedCornerShape(5.dp))
        )
        LazyColumn(
            modifier = modifier,
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(listState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(pickerItems) { index, item ->
                Box(
                    modifier = Modifier.size(
                        width = 88.dp,
                        height = when {
                            centerItemIndex == index -> 26.dp
                            centerItemIndex - 1 == index || centerItemIndex + 1 == index -> 20.dp
                            else -> 18.dp
                        }
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = item,
                        style = when {
                            centerItemIndex == index -> {
                                OnmoimTheme.typography.body1SemiBold.copy(
                                    color = OnmoimTheme.colors.textColor,
                                    textAlign = TextAlign.Center,
                                    fontFeatureSettings = "tnum"
                                )
                            }

                            centerItemIndex - 1 == index || centerItemIndex + 1 == index -> {
                                OnmoimTheme.typography.body2Regular.copy(
                                    color = OnmoimTheme.colors.gray05,
                                    textAlign = TextAlign.Center,
                                    fontFeatureSettings = "tnum"
                                )
                            }

                            else -> {
                                OnmoimTheme.typography.caption1Regular.copy(
                                    color = OnmoimTheme.colors.gray04,
                                    textAlign = TextAlign.Center,
                                    fontFeatureSettings = "tnum"
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PickerPreview() {
    OnmoimTheme {
        Surface {
            Picker(
                items = List(20) { (it + 1).toString() },
                initialValue = "20",
                onValueChange = {}
            )
        }
    }
}