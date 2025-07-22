package com.onmoim.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.shimmerBackground

@Composable
fun MemberListItem(
    onClickTransfer: () -> Unit,
    onClickExpulsion: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    name: String,
    imageUrl: String?,
    isHost: Boolean,
    enabledMenu: Boolean = true
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(imageUrl)
        }.build()
    )
    val painterState by painter.state.collectAsStateWithLifecycle()
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    if (enabledMenu) {
                        showMenu = !showMenu
                    }
                }
            )
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp)
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
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(R.drawable.ic_user),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                if (isHost) {
                    Image(
                        painter = painterResource(R.drawable.ic_crown),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                style = OnmoimTheme.typography.body2Regular.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
        }
        AnimatedVisibility(
            visible = showMenu,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickTransfer
                        )
                        .background(OnmoimTheme.colors.primaryBlue)
                        .size(68.dp, 44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.member_list_item_transfer),
                        style = OnmoimTheme.typography.caption1Bold.copy(
                            color = Color.White
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickExpulsion
                        )
                        .background(OnmoimTheme.colors.accentSoftRed)
                        .size(68.dp, 44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.member_list_item_ban),
                        style = OnmoimTheme.typography.caption1Bold.copy(
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MemberListItemPreview() {
    OnmoimTheme {
        MemberListItem(
            onClickTransfer = {},
            onClickExpulsion = {},
            modifier = Modifier.fillMaxWidth(),
            name = "홍길동",
            imageUrl = null,
            isHost = true
        )
    }
}