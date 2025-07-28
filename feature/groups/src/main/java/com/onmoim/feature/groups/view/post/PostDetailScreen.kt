package com.onmoim.feature.groups.view.post

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.data.constant.PostType
import com.onmoim.core.data.model.Comment
import com.onmoim.core.data.model.Post
import com.onmoim.core.designsystem.component.CommentTextField
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.post.CommentItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.shimmerBackground
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.BoardType
import com.onmoim.feature.groups.state.PostDetailEvent
import com.onmoim.feature.groups.state.PostDetailUiState
import com.onmoim.feature.groups.viewmodel.PostDetailViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PostDetailRoute(
    postDetailViewModel: PostDetailViewModel,
    onNavigateToReply: (commentId: Int) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val postDetailUiState by postDetailViewModel.postDetailUiState.collectAsStateWithLifecycle()
    val commentPagingItems = postDetailViewModel.commentPagingData.collectAsLazyPagingItems()
    val comment by postDetailViewModel.commentState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    PostDetailScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onClickPostMenu = {

        },
        onClickLike = postDetailViewModel::likePostToggle,
        onClickCommentMenu = {

        },
        onClickReply = onNavigateToReply,
        postDetailUiState = postDetailUiState,
        commentPagingItems = commentPagingItems,
        comment = comment,
        onCommentChange = postDetailViewModel::onCommentChange,
        onSendComment = postDetailViewModel::writeComment
    )

    LaunchedEffect(Unit) {
        postDetailViewModel.event.collect { event ->
            when (event) {
                is PostDetailEvent.PostLikeFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                is PostDetailEvent.CommentWriteFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                PostDetailEvent.CommentWriteSuccess -> {
                    commentPagingItems.refresh()
                }
            }
        }
    }
}

@Composable
private fun PostDetailScreen(
    onBack: () -> Unit,
    onClickPostMenu: () -> Unit,
    onClickLike: () -> Unit,
    onClickCommentMenu: () -> Unit,
    onClickReply: (commentId: Int) -> Unit,
    postDetailUiState: PostDetailUiState,
    commentPagingItems: LazyPagingItems<Comment>,
    comment: String,
    onCommentChange: (String) -> Unit,
    onSendComment: () -> Unit
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
        when (postDetailUiState) {
            is PostDetailUiState.Error -> {
                Text(postDetailUiState.t.message.toString())
            }

            PostDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is PostDetailUiState.Success -> {
                val post = postDetailUiState.post
                val loadState = commentPagingItems.loadState.refresh

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    item {
                        PostContent(
                            userName = post.name,
                            profileImageUrl = post.profileImageUrl,
                            boardType = when (post.type) {
                                PostType.ALL -> null
                                PostType.NOTICE -> BoardType.NOTICE
                                PostType.INTRODUCTION -> BoardType.INTRO
                                PostType.REVIEW -> BoardType.REVIEW
                                PostType.FREE -> BoardType.FREE
                            },
                            writeDateTime = post.createdDate,
                            title = post.title,
                            content = post.content,
                            imageUrls = post.imageUrls,
                            likeCount = post.likeCount,
                            isLike = post.isLiked,
                            onClickLike = onClickLike,
                            commentCount = post.commentCount
                        )
                    }
                    when (loadState) {
                        is LoadState.Error -> {
                            item {
                                Text(loadState.error.message.toString())
                            }
                        }

                        LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.padding(vertical = 30.dp)
                                    )
                                }
                            }
                        }

                        is LoadState.NotLoading -> {
                            items(commentPagingItems.itemCount) { index ->
                                commentPagingItems[index]?.let {
                                    CommentItem(
                                        onClickMenu = onClickCommentMenu,
                                        onClickReply = {
                                            onClickReply(it.id)
                                        },
                                        userName = it.userName,
                                        profileImageUrl = it.profileImageUrl,
                                        content = it.content,
                                        replyCount = it.replyCount
                                    )
                                }
                            }
                        }
                    }
                }
                CommentTextField(
                    value = comment,
                    onValueChange = onCommentChange,
                    onClickSend = onSendComment,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun PostContent(
    userName: String,
    profileImageUrl: String?,
    boardType: BoardType?,
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
                text = when (boardType) {
                    BoardType.NOTICE -> stringResource(R.string.notice_board)
                    BoardType.INTRO -> stringResource(R.string.intro_board)
                    BoardType.REVIEW -> stringResource(R.string.review_board)
                    BoardType.FREE -> stringResource(R.string.free_board)
                    else -> ""
                },
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

@Preview(showBackground = true)
@Composable
private fun PostDetailScreenPreview() {
    val sampleComment = Comment(
        id = 1,
        authorId = 1,
        content = "This is a sample comment.",
        createdDate = LocalDateTime.now(),
        modifiedDate = LocalDateTime.now(),
        profileImageUrl = null,
        replyCount = 0,
        userName = "user"
    )
    val commentPagingItems = MutableStateFlow(
        PagingData.from(
            listOf(
                sampleComment,
                sampleComment,
                sampleComment
            )
        )
    ).collectAsLazyPagingItems()

    OnmoimTheme {
        PostDetailScreen(
            onBack = {},
            onClickPostMenu = {},
            onClickLike = {},
            onClickCommentMenu = {},
            onClickReply = {},
            postDetailUiState = PostDetailUiState.Success(
                post = Post(
                    id = 1,
                    commentCount = 3,
                    content = "This is a sample post content.",
                    createdDate = LocalDateTime.now(),
                    modifiedDate = LocalDateTime.now(),
                    imageUrls = emptyList(),
                    isLiked = false,
                    likeCount = 5,
                    name = "John Doe",
                    profileImageUrl = null,
                    title = "Sample Post",
                    type = PostType.FREE
                )
            ),
            commentPagingItems = commentPagingItems,
            comment = "",
            onCommentChange = {},
            onSendComment = {}
        )
    }
}