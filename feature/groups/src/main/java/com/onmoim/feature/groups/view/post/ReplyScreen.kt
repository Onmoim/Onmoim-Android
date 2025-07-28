package com.onmoim.feature.groups.view.post

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onmoim.core.designsystem.component.CommentTextField
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.component.post.CommentItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.viewmodel.ReplyViewModel

@Composable
fun ReplyRoute(
    replyViewModel: ReplyViewModel
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val reply by replyViewModel.replyState.collectAsStateWithLifecycle()

    ReplyScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onClickMenu = {},
        onClickCommentMenu = {},
        onClickReplyMenu = {},
        reply = reply,
        onReplyChange = replyViewModel::onReplyChange,
        onSendReply = replyViewModel::writeReply
    )
}

@Composable
private fun ReplyScreen(
    onBack: () -> Unit,
    onClickMenu: () -> Unit,
    onClickCommentMenu: () -> Unit,
    onClickReplyMenu: (replyId: Int) -> Unit,
    reply: String,
    onReplyChange: (String) -> Unit,
    onSendReply: () -> Unit
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
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                CommentItem(
                    onClickMenu = onClickCommentMenu,
                    userName = "사용자",
                    profileImageUrl = null,
                    content = "댓글 내용",
                    replyCount = 0
                )
            }
            items(10) {
                CommentItem(
                    onClickMenu = {},
                    modifier = Modifier.padding(start = 15.dp),
                    userName = "사용자$it",
                    profileImageUrl = null,
                    content = "댓글 내용",
                    replyCount = 0
                )
            }
        }
        CommentTextField(
            value = reply,
            onValueChange = onReplyChange,
            onClickSend = onSendReply,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ReplyScreenPreview() {
    OnmoimTheme {
        ReplyScreen(
            onBack = {},
            onClickMenu = {},
            onClickCommentMenu = {},
            onClickReplyMenu = {},
            reply = "",
            onReplyChange = {},
            onSendReply = {}
        )
    }
}