package com.onmoim.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.theme.OnmoimTheme

@Composable
fun FilterChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    leadingIcon: Painter? = null
) {
    Row(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .background(
                color = if (selected) {
                    OnmoimTheme.colors.primaryBlue
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 0.5.dp,
                color = OnmoimTheme.colors.gray04,
                shape = RoundedCornerShape(10.dp)
            )
            .height(32.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (leadingIcon != null) {
            Image(
                painter = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = label,
            style = OnmoimTheme.typography.caption1Regular.copy(
                color = if (selected) {
                    Color.White
                } else {
                    OnmoimTheme.colors.textColor
                }
            )
        )
    }
}

@Preview
@Composable
private fun FilterChipPreview() {
    OnmoimTheme {
        FilterChip(
            onClick = {},
            label = "번개",
            selected = false
        )
    }
}

@Preview
@Composable
private fun FilterChipSelectedPreview() {
    OnmoimTheme {
        FilterChip(
            onClick = {},
            label = "번개",
            selected = true
        )
    }
}

@Preview
@Composable
private fun FilterChipForLeadingPreview() {
    OnmoimTheme {
        FilterChip(
            onClick = {},
            label = "번개",
            selected = false,
            leadingIcon = painterResource(R.drawable.ic_lightning)
        )
    }
}