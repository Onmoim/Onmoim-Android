package com.onmoim.feature.groups.view.post

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.onmoim.core.data.model.Comment
import com.onmoim.core.data.model.CommentThread
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.SendTextField
import com.onmoim.core.designsystem.component.post.CommentItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.state.ReplyEvent
import com.onmoim.feature.groups.viewmodel.ReplyViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

@Composable
fun ReplyRoute(
    replyViewModel: ReplyViewModel
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val reply by replyViewModel.replyState.collectAsStateWithLifecycle()
    val commentThreadPagingItems = replyViewModel.commentThreadPagingData.collectAsLazyPagingItems()
    val context = LocalContext.current

    ReplyScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onClickMenu = {},
        onClickCommentMenu = {},
        onClickReplyMenu = {},
        reply = reply,
        onReplyChange = replyViewModel::onReplyChange,
        onSendReply = replyViewModel::writeReply,
        commentThreadPagingItems = commentThreadPagingItems
    )

    LaunchedEffect(Unit) {
        replyViewModel.event.collect { event ->
            when (event) {
                is ReplyEvent.WriteReplyFailure -> {
                    Toast.makeText(context, event.t.message, Toast.LENGTH_SHORT).show()
                }

                ReplyEvent.WriteReplySuccess -> {
                    commentThreadPagingItems.refresh()
                }
            }
        }
    }
}

@Composable
private fun ReplyScreen(
    onBack: () -> Unit,
    onClickMenu: () -> Unit,
    onClickCommentMenu: () -> Unit,
    onClickReplyMenu: (replyId: Int) -> Unit,
    reply: String,
    onReplyChange: (String) -> Unit,
    onSendReply: () -> Unit,
    commentThreadPagingItems: LazyPagingItems<CommentThread>
) {
    val loadState = commentThreadPagingItems.loadState.refresh

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
            .imePadding()
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.reply_post_reply),
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
                CommonAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.reply_post_reply),
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
                            onClick = onClickMenu
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_more_vertical),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        )
        when {
            loadState is LoadState.Error -> {
                Text(loadState.error.message.toString())
            }

            commentThreadPagingItems.itemCount == 0 && loadState == LoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(commentThreadPagingItems.itemCount) { index ->
                        commentThreadPagingItems[index]?.let { commentThread ->
                            val comment = commentThread.comment

                            CommentItem(
                                onClickMenu = {},
                                modifier = Modifier.padding(
                                    start = if (commentThread.isParent) {
                                        0.dp
                                    } else {
                                        15.dp
                                    }
                                ),
                                userName = comment.userName,
                                profileImageUrl = comment.profileImageUrl,
                                content = comment.content,
                                replyCount = comment.replyCount
                            )
                        }
                    }
                }
                SendTextField(
                    value = reply,
                    onValueChange = onReplyChange,
                    onClickSend = onSendReply,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun ReplyScreenPreview() {
    val sampleCommentThread = CommentThread(
        isParent = false,
        comment = Comment(
            id = 1,
            authorId = 1,
            content = "This is a sample comment.",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
            profileImageUrl = null,
            replyCount = 0,
            userName = "user"
        )
    )
    val commentPagingItems = MutableStateFlow(
        PagingData.from(
            listOf(
                sampleCommentThread.copy(isParent = true),
                sampleCommentThread,
                sampleCommentThread,
                sampleCommentThread,
                sampleCommentThread,
                sampleCommentThread
            )
        )
    ).collectAsLazyPagingItems()

    OnmoimTheme {
        ReplyScreen(
            onBack = {},
            onClickMenu = {},
            onClickCommentMenu = {},
            onClickReplyMenu = {},
            reply = "",
            onReplyChange = {},
            onSendReply = {},
            commentThreadPagingItems = commentPagingItems
        )
    }
}