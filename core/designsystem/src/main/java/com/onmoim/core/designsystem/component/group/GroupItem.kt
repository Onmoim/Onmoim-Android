package com.onmoim.core.designsystem.component.group

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.shimmerBackground

@Composable
fun GroupItem(
    onClick: () -> Unit,
    imageUrl: String?,
    title: String,
    location: String,
    memberCount: Int,
    scheduleCount: Int,
    categoryName: String,
    isRecommended: Boolean,
    isSignUp: Boolean,
    isOperating: Boolean,
    isFavorite: Boolean,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(imageUrl)
        }.build()
    )
    val painterState by painter.state.collectAsStateWithLifecycle()

    Column {
        Row(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick
                )
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Crossfade(
                    targetState = painterState
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
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFA4A4A4))
                            )
                        }
                    }
                }
                Image(
                    painter = painterResource(
                        id = if (isFavorite) {
                            R.drawable.ic_favorite_enabled
                        } else {
                            R.drawable.ic_favorite_disabled
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomStart)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    MeetChip(
                        label = categoryName,
                        backgroundColor = Color(0xFFF4F4F4)
                    )
                    if (isRecommended) {
                        MeetChip(
                            label = stringResource(R.string.meet_item_recommend),
                            backgroundColor = Color(0xFFFFE6FC)
                        )
                    }
                    if (isSignUp) {
                        MeetChip(
                            label = stringResource(R.string.meet_item_signing_up),
                            backgroundColor = Color(0xFFBDEEE7)
                        )
                    }
                    if (isOperating) {
                        MeetChip(
                            label = stringResource(R.string.meet_item_operating),
                            backgroundColor = Color(0xFFF97D7D),
                            textColor = Color.White
                        )
                    }
                }
                Text(
                    text = title,
                    style = OnmoimTheme.typography.body2Bold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = location,
                        style = OnmoimTheme.typography.caption2Regular.copy(
                            color = OnmoimTheme.colors.gray05
                        )
                    )
                    Box(
                        Modifier
                            .padding(horizontal = 4.dp)
                            .size(2.dp)
                            .background(
                                color = OnmoimTheme.colors.gray05,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = stringResource(
                            id = R.string.meet_item_member_count,
                            memberCount
                        ),
                        style = OnmoimTheme.typography.caption2Regular.copy(
                            color = OnmoimTheme.colors.gray05
                        )
                    )
                    Box(
                        Modifier
                            .padding(horizontal = 4.dp)
                            .size(2.dp)
                            .background(
                                color = OnmoimTheme.colors.gray05,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = stringResource(
                            id = R.string.meet_item_schedule_count,
                            scheduleCount
                        ),
                        style = OnmoimTheme.typography.caption2SemiBold.copy(
                            color = OnmoimTheme.colors.gray06
                        )
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = OnmoimTheme.colors.gray02
        )
    }
}

@Composable
private fun MeetChip(
    label: String,
    backgroundColor: Color,
    textColor: Color = Color(0xFF675F60),
) {
    Box(
        modifier = Modifier.background(
            color = backgroundColor,
            shape = RoundedCornerShape(16.dp)
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = OnmoimTheme.typography.caption2Regular.copy(
                color = textColor
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MeetItemPreview() {
    OnmoimTheme {
        GroupItem(
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