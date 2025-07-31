package com.onmoim.feature.groups.view.mygroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.ui.shimmerBackground
import java.time.LocalDateTime

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

    Row(
        modifier = modifier
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
    }
}

@Preview
@Composable
fun ChatRoomItemPreview() {
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