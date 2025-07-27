package com.onmoim.feature.groups.view.post

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.designsystem.component.CommentTextField
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.shimmerBackground
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.BoardType
import com.onmoim.feature.groups.viewmodel.PostDetailViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PostDetailRoute(
    postDetailViewModel: PostDetailViewModel
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    PostDetailScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onClickPostMenu = {

        },
        onClickCommentMenu = {

        }
    )
}

@Composable
private fun PostDetailScreen(
    onBack: () -> Unit,
    onClickPostMenu: () -> Unit,
    onClickCommentMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
            .imePadding()
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.post),
                    style = OnmoimTheme.typography.body1SemiBold
                )
            },
            navigationIcon = {
                NavigationIconButton(
                    onClick = onBack
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            },
            actions = {
                NavigationIconButton(
                    onClick = onClickPostMenu
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vertical),
                        contentDescription = null
                    )
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            item {
                PostContent(
                    userName = "username",
                    profileImageUrl = "https://picsum.photos/200",
                    boardType = BoardType.REVIEW,
                    writeDateTime = LocalDateTime.now(),
                    title = "title",
                    content = "content",
                    imageUrls = listOf("https://picsum.photos/200"),
                    likeCount = 10,
                    isLike = false,
                    onClickLike = {},
                    commentCount = 20
                )
            }
            items(5) {
                CommentItem(
                    onClickMenu = {},
                    onClickReply = {},
                    userName = "username",
                    profileImageUrl = "https://picsum.photos/200",
                    content = "content",
                    commentCount = it
                )
            }
        }
        CommentTextField(
            value = "",
            onValueChange = {},
            onClickSend = {},
            modifier = Modifier.fillMaxWidth(),
            onSend = {}
        )
    }
}

@Composable
private fun PostContent(
    userName: String,
    profileImageUrl: String?,
    boardType: BoardType,
    writeDateTime: LocalDateTime,
    title: String,
    content: String,
    imageUrls: List<String>,
    likeCount: Int,
    isLike: Boolean,
    onClickLike: () -> Unit,
    commentCount: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(36.dp)
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
                Text(
                    text = userName,
                    style = OnmoimTheme.typography.body2SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
            }
            Text(
                text = stringResource(
                    id = when (boardType) {
                        BoardType.NOTICE -> R.string.notice_board
                        BoardType.INTRO -> R.string.intro_board
                        BoardType.REVIEW -> R.string.review_board
                        BoardType.FREE -> R.string.free_board
                    }
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(80.dp),
                style = OnmoimTheme.typography.caption1SemiBold.copy(
                    color = OnmoimTheme.colors.primaryBlue,
                    textAlign = TextAlign.End
                )
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h:mm").format(writeDateTime),
            modifier = Modifier.padding(horizontal = 15.dp),
            style = OnmoimTheme.typography.caption2Regular.copy(
                color = OnmoimTheme.colors.gray05
            )
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 15.dp),
            style = OnmoimTheme.typography.body1SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = content,
            modifier = Modifier.padding(horizontal = 15.dp),
            style = OnmoimTheme.typography.body2Regular.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Spacer(Modifier.height(if (imageUrls.isEmpty()) 8.dp else 16.dp))
        imageUrls.forEachIndexed { index, url ->
            val imagePainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current).apply {
                    data(url)
                }.build()
            )
            val imagePainterState by imagePainter.state.collectAsStateWithLifecycle()

            Box(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .aspectRatio(33 / 20f)
            ) {
                when (imagePainterState) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .shimmerBackground()
                        )
                    }

                    is AsyncImagePainter.State.Success -> {
                        Image(
                            painter = imagePainter,
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
            Spacer(Modifier.height(if (index == imageUrls.lastIndex) 8.dp else 16.dp))
        }
        Row(
            modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onClickLike
                    )
                    .height(36.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(
                        id = if (isLike) {
                            R.drawable.ic_like_filled
                        } else {
                            R.drawable.ic_like
                        }
                    ),
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
            }
            Row(
                modifier = Modifier.height(36.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
        }
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = OnmoimTheme.colors.gray02
        )
    }
}

@Composable
private fun CommentItem(
    onClickMenu: () -> Unit,
    onClickReply: () -> Unit,
    userName: String,
    profileImageUrl: String?,
    content: String,
    commentCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp)
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
                Text(
                    text = if (commentCount > 0) {
                        stringResource(R.string.post_detail_more_reply, commentCount)
                    } else {
                        stringResource(R.string.post_detail_write_reply)
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

@Preview
@Composable
private fun PostDetailScreenPreview() {
    OnmoimTheme {
        PostDetailScreen(
            onBack = {},
            onClickPostMenu = {},
            onClickCommentMenu = {}
        )
    }
}