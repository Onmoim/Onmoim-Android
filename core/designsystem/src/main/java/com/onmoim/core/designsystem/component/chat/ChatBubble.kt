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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.util.Locale

@Composable
private fun BaseChatBubble(
    isMe: Boolean,
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (isMe) {
                    Color(0xFFE5F0FF)
                } else {
                    OnmoimTheme.colors.backgroundColor
                },
                shape = if (isMe) {
                    RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp)
                } else {
                    RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp)
                }
            )
            .padding(
                horizontal = 10.dp,
                vertical = 7.dp
            )
    ) {
        Text(
            text = message,
            style = OnmoimTheme.typography.caption1Regular.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
    }
}

@Composable
fun ChatBubbleWithProfile(
    onClickProfile: () -> Unit,
    modifier: Modifier = Modifier,
    userName: String,
    message: String,
    sendDateTime: LocalDateTime,
    profileImageUrl: String?,
    isOwner: Boolean,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(profileImageUrl)
        }.build()
    )
    val painterState by painter.state.collectAsStateWithLifecycle()

    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickProfile
                )
                .size(39.dp)
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
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Image(
                        painter = painterResource(R.drawable.ic_user),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
            if (isOwner) {
                Image(
                    painter = painterResource(R.drawable.ic_crown),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 3.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = userName,
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            Spacer(Modifier.height(3.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                BaseChatBubble(
                    isMe = false,
                    message = message,
                    modifier = Modifier.widthIn(max = 198.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = DateTimeFormatter.ofPattern(
                        "a h:mm",
                        Locale.KOREAN
                    ).format(sendDateTime),
                    style = OnmoimTheme.typography.caption3Regular.copy(
                        color = Color(0xFF949494)
                    )
                )
            }
        }
    }
}

@Composable
fun MyChatBubble(
    message: String,
    sendDateTime: LocalDateTime,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = DateTimeFormatter.ofPattern(
                "a h:mm",
                Locale.KOREAN
            ).format(sendDateTime),
            style = OnmoimTheme.typography.caption3Regular.copy(
                color = Color(0xFF949494)
            )
        )
        Spacer(Modifier.width(8.dp))
        BaseChatBubble(
            isMe = true,
            message = message,
            modifier = Modifier.widthIn(max = 243.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatBubblePreview() {
    OnmoimTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MyChatBubble(
                message = "안녕하세요",
                sendDateTime = LocalDateTime.now(),
                modifier = Modifier.align(Alignment.End)
            )
            ChatBubbleWithProfile(
                onClickProfile = {},
                userName = "홍길동",
                message = "안녕하세요",
                sendDateTime = LocalDateTime.now(),
                profileImageUrl = null,
                isOwner = false
            )
            ChatBubbleWithProfile(
                onClickProfile = {},
                userName = "김길동",
                message = "안녕하세요",
                sendDateTime = LocalDateTime.now(),
                profileImageUrl = null,
                isOwner = true
            )
        }
    }
}