package com.onmoim.core.designsystem.component.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ChatRoomItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String?,
    memberCount: Int,
    lastMessage: String,
    lastMessageDateTime: LocalDateTime,
    unread: Boolean
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(imageUrl)
        }.build()
    )
    val painterState by painter.state.collectAsStateWithLifecycle()
    val nowDateTime = LocalDateTime.now()
    val periodHours = ChronoUnit.HOURS.between(lastMessageDateTime, nowDateTime)
    val periodDays = ChronoUnit.DAYS.between(lastMessageDateTime, nowDateTime)

    Row(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .heightIn(min = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        ) {
            when (painterState) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .shimmerBackground()
                    )
                }

                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color(0xFFA4A4A4))
                    )
                }
            }
        }
        Spacer(Modifier.width(15.dp))
        Column(
            modifier = Modifier
                .padding(top = 4.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text(
                        text = title,
                        style = OnmoimTheme.typography.body2SemiBold.copy(
                            color = OnmoimTheme.colors.textColor
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = memberCount.toString(),
                        style = OnmoimTheme.typography.body2SemiBold.copy(
                            color = OnmoimTheme.colors.gray05
                        )
                    )
                }
                Text(
                    text = when {
                        periodHours < 1 -> stringResource(R.string.chat_room_item_now)
                        periodHours < 24 -> stringResource(R.string.chat_room_item_hours_ago, periodHours)
                        periodDays < 3 -> stringResource(R.string.chat_room_item_days_ago, periodDays)
                        else -> DateTimeFormatter.ofPattern("yyyy.MM.dd")
                            .format(lastMessageDateTime)
                    },
                    style = OnmoimTheme.typography.caption2Regular.copy(
                        color = OnmoimTheme.colors.gray05
                    )
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lastMessage,
                    modifier = Modifier.weight(1f),
                    style = OnmoimTheme.typography.caption1Regular.copy(
                        color = OnmoimTheme.colors.gray05
                    )
                )
                Spacer(Modifier.width(41.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (unread) {
                                OnmoimTheme.colors.accentSoftRed
                            } else {
                                Color.Transparent
                            },
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChatRoomItemPreview() {
    OnmoimTheme {
        ChatRoomItem(
            onClick = {},
            modifier = Modifier,
            title = "Chat Room Title",
            imageUrl = null,
            memberCount = 10,
            lastMessage = "Hello, world!",
            lastMessageDateTime = LocalDateTime.now(),
            unread = true
        )
    }
}