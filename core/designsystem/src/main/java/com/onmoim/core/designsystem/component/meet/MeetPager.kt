package com.onmoim.core.designsystem.component.meet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.theme.OnmoimTheme

@Composable
fun MeetPager(
    title: String,
    pageSize: Int,
    itemsPerPage: Int,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 16.dp,
    onClickMore: () -> Unit,
    meetContent: @Composable (page: Int, index: Int) -> Unit
) {
    val pagerState = rememberPagerState { pageSize }
    val moreButtonBottomBorderColor = OnmoimTheme.colors.gray01

    Column(modifier = modifier) {
        MeetHeader(
            title = title,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 15.dp
            ),
            pageSpacing = 12.dp
        ) { page ->
            Column(
                verticalArrangement = Arrangement.spacedBy(itemSpacing)
            ) {
                List(itemsPerPage) { index ->
                    meetContent(page, index)
                }
            }
        }
        Text(
            text = stringResource(R.string.more_show),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickMore
                )
                .fillMaxWidth()
                .drawBehind {
                    val rectHeight = 5.dp.toPx()
                    drawRect(
                        color = moreButtonBottomBorderColor,
                        topLeft = Offset(0f, size.height - rectHeight),
                        size = size.copy(height = rectHeight)
                    )
                }
                .padding(vertical = 20.dp),
            style = OnmoimTheme.typography.body2Regular.copy(
                color = OnmoimTheme.colors.gray05,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MeetPagerPreview() {
    OnmoimTheme {
        MeetPager(
            title = "title",
            pageSize = 3,
            itemsPerPage = 4,
            onClickMore = {}
        ) { page, index ->
            MeetItem(
                onClick = {},
                imageUrl = "",
                title = "title",
                location = "location",
                memberCount = 123,
                scheduleCount = 123,
                categoryName = "categoryName",
                isRecommended = true,
                isSignUp = true,
                isOperating = true,
                isFavorite = true
            )
        }
    }
}