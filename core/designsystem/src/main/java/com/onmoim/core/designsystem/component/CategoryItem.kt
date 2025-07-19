package com.onmoim.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.onmoim.core.designsystem.theme.shadow1

@Composable
fun CategoryItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean,
    imageUrl: String?,
    label: String
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(imageUrl)
        }.build()
    )
    val state by painter.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .shadow1(999.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(50.dp)
            ) {
                when (state) {
                    is AsyncImagePainter.State.Error -> {
                        // TODO: 추후 구현
                    }

                    is AsyncImagePainter.State.Success -> {
                        Image(
                            painter = painter,
                            contentDescription = label,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    else -> {}
                }
            }
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(
                            color = Color(0xFF565656).copy(alpha = 0.8f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            style = OnmoimTheme.typography.caption2Regular.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    OnmoimTheme {
        CategoryItem(
            onClick = {},
            selected = false,
            imageUrl = null,
            label = "카테고리"
        )
    }
}