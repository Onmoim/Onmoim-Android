package com.onmoim.feature.groups.view.groupdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.designsystem.component.group.PostCard
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.shimmerBackground
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.constant.GroupDetailPostFilter
import com.onmoim.feature.groups.constant.GroupDetailPostViewMode
import java.time.LocalDateTime

@Composable
fun GroupDetailPostContainer(
    onClickPost: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedFilter: GroupDetailPostFilter,
    onFilterChange: (GroupDetailPostFilter) -> Unit,
    initialViewMode: GroupDetailPostViewMode = GroupDetailPostViewMode.POST
) {
    var selectedViewMode by remember { mutableStateOf(initialViewMode) }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = OnmoimTheme.colors.gray01
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GroupDetailPostFilter.entries.forEach { filter ->
                Text(
                    text = stringResource(
                        id = when (filter) {
                            GroupDetailPostFilter.ALL -> R.string.group_detail_post_all
                            GroupDetailPostFilter.NOTICE -> R.string.group_detail_post_notice
                            GroupDetailPostFilter.REG_GREETING -> R.string.group_detail_post_reg_greeting
                            GroupDetailPostFilter.MEET_REVIEW -> R.string.group_detail_post_meet_review
                            GroupDetailPostFilter.FREE_BOARD -> R.string.group_detail_post_free_board
                        }
                    ),
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                onFilterChange(filter)
                            }
                        )
                        .padding(horizontal = 15.5.dp, vertical = 15.dp),
                    style = OnmoimTheme.typography.caption1Regular.copy(
                        color = if (selectedFilter == filter) {
                            OnmoimTheme.colors.textColor
                        } else {
                            OnmoimTheme.colors.gray04
                        }
                    )
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            selectedViewMode = when (selectedViewMode) {
                                GroupDetailPostViewMode.POST -> GroupDetailPostViewMode.ALBUM
                                GroupDetailPostViewMode.ALBUM -> GroupDetailPostViewMode.POST
                            }
                        }
                    )
                    .padding(horizontal = 15.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sort),
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(
                        id = when (selectedViewMode) {
                            GroupDetailPostViewMode.POST -> R.string.group_detail_post_view_mode_post
                            GroupDetailPostViewMode.ALBUM -> R.string.group_detail_post_view_mode_album
                        }
                    ),
                    style = OnmoimTheme.typography.caption2Regular.copy(
                        color = OnmoimTheme.colors.gray06
                    )
                )
            }
        }
        when (selectedViewMode) {
            GroupDetailPostViewMode.POST -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(10) {
                        PostCard(
                            onClick = {
                                // TODO: api 연동시 수정
                                onClickPost(it)
                            },
                            userName = "userName",
                            profileImgUrl = "https://picsum.photos/200",
                            writeDateTime = LocalDateTime.now(),
                            title = "title",
                            content = "content",
                            likeCount = 10,
                            commentCount = 20,
                            representImageUrl = "https://picsum.photos/200"
                        )
                    }
                }
            }

            GroupDetailPostViewMode.ALBUM -> {
                LazyVerticalGrid(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(10) {
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current).apply {
                                data("https://picsum.photos/200")
                            }.build()
                        )
                        val painterState by painter.state.collectAsStateWithLifecycle()

                        Box(
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        // TODO: api 연동시 수정
                                        onClickPost(it)
                                    }
                                )
                                .aspectRatio(1f)
                        ) {
                            when (painterState) {
                                is AsyncImagePainter.State.Loading -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .shimmerBackground()
                                    )
                                }

                                is AsyncImagePainter.State.Success -> {
                                    Image(
                                        painter = painter,
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
                    }
                }
            }
        }
    }
}