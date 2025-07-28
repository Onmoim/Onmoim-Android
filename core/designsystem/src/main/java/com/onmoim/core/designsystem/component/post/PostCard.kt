package com.onmoim.core.designsystem.component.post

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
fun PostCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userName: String,
    profileImgUrl: String?,
    writeDateTime: LocalDateTime,
    title: String,
    content: String,
    likeCount: Int,
    commentCount: Int,
    representImageUrl: String?
) {
    val profilePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(profileImgUrl)
        }.build()
    )
    val representPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(representImageUrl)
        }.build()
    )
    val profilePainterState by profilePainter.state.collectAsStateWithLifecycle()
    val representPainterState by representPainter.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .then(modifier)
            .padding(
                horizontal = 15.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(32.dp)
            ) {
                when (profilePainterState) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .shimmerBackground()
                                .clip(CircleShape)
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
            Text(
                text = userName,
                modifier = Modifier.weight(1f),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            Spacer(Modifier.width(13.dp))
            Text(
                text = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN)
                    .format(writeDateTime),
                style = OnmoimTheme.typography.caption2Regular.copy(
                    color = OnmoimTheme.colors.gray05
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = title,
            style = OnmoimTheme.typography.body1SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = content,
            style = OnmoimTheme.typography.body2Regular.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Spacer(Modifier.height(16.dp))
        if (representImageUrl != null) {
            Crossfade(
                targetState = representPainterState,
                modifier = Modifier.aspectRatio(33 / 20f)
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
            Spacer(Modifier.height(16.dp))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_like),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = likeCount.toString(),
                style = OnmoimTheme.typography.caption2Regular.copy(
                    color = OnmoimTheme.colors.gray06,
                    fontFeatureSettings = "tnum"
                )
            )
            Spacer(Modifier.width(12.dp))
            Image(
                painter = painterResource(R.drawable.ic_comment),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = commentCount.toString(),
                style = OnmoimTheme.typography.caption2Regular.copy(
                    color = OnmoimTheme.colors.gray06,
                    fontFeatureSettings = "tnum"
                )
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    top = 24.dp,
                    bottom = 16.dp
                )
                .fillMaxWidth(),
            thickness = 1.dp,
            color = OnmoimTheme.colors.gray02
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PostCardPreview() {
    OnmoimTheme {
        PostCard(
            onClick = {},
            userName = "userName",
            profileImgUrl = null,
            writeDateTime = LocalDateTime.now(),
            title = "title",
            content = "content",
            likeCount = 10,
            commentCount = 20,
            representImageUrl = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PostCardForImagePreview() {
    OnmoimTheme {
        PostCard(
            onClick = {},
            userName = "userName",
            profileImgUrl = "",
            writeDateTime = LocalDateTime.now(),
            title = "title",
            content = "content",
            likeCount = 10,
            commentCount = 20,
            representImageUrl = ""
        )
    }
}