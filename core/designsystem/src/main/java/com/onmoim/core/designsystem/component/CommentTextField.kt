package com.onmoim.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.theme.OnmoimTheme

@Composable
fun CommentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClickSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = OnmoimTheme.colors.gray04

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(OnmoimTheme.colors.backgroundColor)
            .drawBehind {
                drawLine(
                    color = borderColor,
                    strokeWidth = 0.5.dp.toPx(),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f)
                )
            }
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .navigationBarsPadding()
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .weight(1f)
                .height(32.dp),
            textStyle = OnmoimTheme.typography.body2Regular.copy(
                color = OnmoimTheme.colors.textColor
            ),
            singleLine = false
        ) { innerTextField ->
            Box(
                modifier = Modifier
                    .background(
                        color = OnmoimTheme.colors.gray01,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.comment_text_field_hint),
                        style = OnmoimTheme.typography.body2Regular.copy(
                            color = OnmoimTheme.colors.gray04
                        )
                    )
                }
                innerTextField()
            }
        }
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clickable(
                    enabled = value.isNotBlank()
                ) {
                    onClickSend()
                }
                .background(
                    color = if (value.isNotBlank()) {
                        OnmoimTheme.colors.primaryBlue
                    } else {
                        OnmoimTheme.colors.gray04
                    },
                    shape = RoundedCornerShape(10.dp)
                )
                .size(60.dp, 32.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.send),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = Color.White
                )
            )
        }
    }
}

@Preview
@Composable
private fun CommentTextFieldPreview() {
    OnmoimTheme {
        CommentTextField(
            value = "",
            onValueChange = {},
            onClickSend = {}
        )
    }
}