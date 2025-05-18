package com.onmoim.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.onmoim.core.ui.theme.NavigationIconSize

@Composable
fun NavigationIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = NavigationIconSize,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Box(
        modifier =
            modifier
                .size(size)
                .clip(CircleShape)
                .clickable(
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Button,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple()
                ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}