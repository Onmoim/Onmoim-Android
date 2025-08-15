package com.onmoim.feature.groups.view.groupdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.onmoim.core.data.constant.SocketConnectionState
import com.onmoim.core.data.model.Message
import com.onmoim.core.designsystem.component.SendTextField
import com.onmoim.core.designsystem.component.chat.ChatBubbleWithProfile
import com.onmoim.core.designsystem.component.chat.MyChatBubble
import com.onmoim.feature.groups.R

@Composable
fun GroupDetailChatContainer(
    modifier: Modifier = Modifier,
    userId: Int,
    chatConnectionState: SocketConnectionState,
    prevChatMessagePagingItems: LazyPagingItems<Message>,
    newChatMessages: List<Message>,
    onClickProfile: (userId: Int) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    onClickSend: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                vertical = 16.dp,
                horizontal = 15.dp
            ),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = prevChatMessagePagingItems.itemCount,
                key = prevChatMessagePagingItems.itemKey { it.messageSequence }
            ) { index ->
                prevChatMessagePagingItems[index]?.let { message ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (message.senderId == userId) {
                            MyChatBubble(
                                message = message.content,
                                sendDateTime = message.sendDateTime,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        } else {
                            ChatBubbleWithProfile(
                                onClickProfile = {
                                    onClickProfile(message.senderId)
                                },
                                modifier = Modifier.align(Alignment.CenterStart),
                                userName = message.userName,
                                message = message.content,
                                sendDateTime = message.sendDateTime,
                                profileImageUrl = message.profileImageUrl,
                                isOwner = message.isOwner
                            )
                        }
                    }
                }
            }
            items(
                items = newChatMessages,
                key = { it.messageSequence }
            ) { message ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (message.senderId == userId) {
                        MyChatBubble(
                            message = message.content,
                            sendDateTime = message.sendDateTime,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    } else {
                        ChatBubbleWithProfile(
                            onClickProfile = {
                                onClickProfile(message.senderId)
                            },
                            modifier = Modifier.align(Alignment.CenterStart),
                            userName = message.userName,
                            message = message.content,
                            sendDateTime = message.sendDateTime,
                            profileImageUrl = message.profileImageUrl,
                            isOwner = message.isOwner
                        )
                    }
                }
            }
        }
        SendTextField(
            value = message,
            onValueChange = onMessageChange,
            onClickSend = onClickSend,
            modifier = Modifier
                .pointerInteropFilter {
                    chatConnectionState != SocketConnectionState.Connected
                }
                .fillMaxWidth(),
            placeHolder = stringResource(R.string.group_detail_send_message_hint)
        )
    }
}