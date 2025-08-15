package com.onmoim.feature.groups.view.mygroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.onmoim.core.data.model.ChatRoom
import com.onmoim.core.designsystem.component.chat.ChatRoomItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

@Composable
fun GroupChatContainer(
    modifier: Modifier = Modifier,
    chatRoomPagingItems: LazyPagingItems<ChatRoom>,
    onClickChatRoom: (groupId: Int) -> Unit
) {
    val borderColor = OnmoimTheme.colors.gray02

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(chatRoomPagingItems.itemCount) { index ->
            chatRoomPagingItems[index]?.let {
                ChatRoomItem(
                    onClick = {
                        onClickChatRoom(it.groupId)
                    },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .drawBehind {
                            if (index < 9) {
                                drawLine(
                                    color = borderColor,
                                    strokeWidth = 1.dp.toPx(),
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height)
                                )
                            }
                        },
                    title = it.title,
                    imageUrl = it.imageUrl,
                    memberCount = it.roomMemberCount,
                    lastMessage = it.lastSentMessage,
                    lastMessageDateTime = it.lastSentDateTime,
                    unread = false // TODO: 추후 구현
                )
            }
        }
        item {
            HorizontalDivider(
                thickness = 5.dp,
                color = OnmoimTheme.colors.gray01
            )
        }
    }
}

@Preview
@Composable
private fun GroupChatContainerPreview() {
    val chatRoom = MutableStateFlow(
        PagingData.from(List(10) {
            ChatRoom(
                groupId = it,
                title = "title $it",
                lastSentMessage = "last sent message $it",
                lastSentDateTime = LocalDateTime.now(),
                roomMemberCount = 10,
                imageUrl = null
            )
        })
    ).collectAsLazyPagingItems()

    OnmoimTheme {
        GroupChatContainer(
            chatRoomPagingItems = chatRoom,
            onClickChatRoom = {}
        )
    }
}