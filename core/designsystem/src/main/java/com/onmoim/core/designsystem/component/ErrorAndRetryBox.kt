package com.onmoim.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.R

@Composable
fun ErrorAndRetryBox(
    onClickRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    content: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = OnmoimTheme.typography.body1SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = content,
            style = OnmoimTheme.typography.body2Regular.copy(
                color = OnmoimTheme.colors.gray04
            )
        )
        Spacer(Modifier.height(44.dp))
        Text(
            text = stringResource(R.string.refresh),
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClickRefresh
            ),
            style = OnmoimTheme.typography.caption1Regular.copy(
                color = OnmoimTheme.colors.gray06,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}