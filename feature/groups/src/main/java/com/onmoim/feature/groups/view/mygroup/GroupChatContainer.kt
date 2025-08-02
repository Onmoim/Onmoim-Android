package com.onmoim.feature.groups.view.mygroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.onmoim.core.designsystem.component.chat.ChatRoomItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import java.time.LocalDateTime

@Composable
fun GroupChatContainer(
    modifier: Modifier = Modifier
) {
    val borderColor = OnmoimTheme.colors.gray02

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(10) { index ->
            Column {
                ChatRoomItem(
                    onClick = {},
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
                    title = "title$index",
                    imageUrl = "https://picsum.photos/200",
                    memberCount = 10,
                    lastMessage = "lastMessage$index",
                    lastMessageDateTime = LocalDateTime.now(),
                    unread = index % 2 == 0
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
    OnmoimTheme {
        GroupChatContainer()
    }
}