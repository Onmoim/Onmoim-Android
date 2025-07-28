package com.onmoim.core.designsystem.component.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.shimmerBackground

@Composable
fun CommentItem(
    onClickMenu: () -> Unit,
    modifier: Modifier = Modifier,
    userName: String,
    profileImageUrl: String?,
    content: String,
    replyCount: Int,
    onClickReply: (() -> Unit)? = null,
) {
    val borderColor = OnmoimTheme.colors.gray02

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 7.dp)
            .drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width - 8.dp.toPx(), size.height),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
    ) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .weight(1f)
        ) {
            Box(
                modifier = Modifier.size(40.dp)
            ) {
                val profilePainter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current).apply {
                        data(profileImageUrl)
                    }.build()
                )
                val profilePainterState by profilePainter.state.collectAsStateWithLifecycle()

                when (profilePainterState) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .shimmerBackground()
                        )
                    }

                    is AsyncImagePainter.State.Success -> {
                        Image(
                            painter = profilePainter,
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
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = userName,
                    style = OnmoimTheme.typography.caption1Regular.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = content,
                    style = OnmoimTheme.typography.caption1Regular.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                if (onClickReply != null) {
                    Text(
                        text = if (replyCount > 0) {
                            stringResource(R.string.comment_more_reply, replyCount)
                        } else {
                            stringResource(R.string.comment_write_reply)
                        },
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = onClickReply
                            )
                            .padding(vertical = 8.dp),
                        style = OnmoimTheme.typography.caption2Regular.copy(
                            color = OnmoimTheme.colors.gray05
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                } else {
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
        Box(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClickMenu
            ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_more_vertical_16),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp)
            )
        }
    }
}