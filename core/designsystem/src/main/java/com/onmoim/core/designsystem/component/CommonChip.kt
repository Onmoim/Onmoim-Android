package com.onmoim.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.theme.OnmoimTheme

@Composable
fun CommonChip(
    label: String,
    backgroundColor: Color = OnmoimTheme.colors.gray02,
    textColor: Color = Color(0xFF675F60),
    shape: Shape = RoundedCornerShape(16.dp)
) {
    Box(
        modifier = Modifier.background(
            color = backgroundColor,
            shape = shape
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = OnmoimTheme.typography.caption2Regular.copy(
                color = textColor
            )
        )
    }
}