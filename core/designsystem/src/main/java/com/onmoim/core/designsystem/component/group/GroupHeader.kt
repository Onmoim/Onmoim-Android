package com.onmoim.core.designsystem.component.group

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.theme.OnmoimTheme

@Composable
fun GroupHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(vertical = 21.5.dp)
            .fillMaxWidth()
            .then(modifier),
        style = OnmoimTheme.typography.body1SemiBold.copy(
            color = OnmoimTheme.colors.textColor
        )
    )
}