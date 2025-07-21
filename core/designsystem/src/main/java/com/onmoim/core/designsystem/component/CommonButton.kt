package com.onmoim.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.theme.OnmoimTheme

@Composable
fun CommonButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.confirm),
    enabled: Boolean = true,
    enabledContainerColor: Color = OnmoimTheme.colors.primaryBlue,
    disabledContainerColor: Color = OnmoimTheme.colors.gray05,
) {
    Box(
        modifier = modifier
            .size(240.dp, 38.dp)
            .background(
                color = if (enabled) {
                    enabledContainerColor
                } else {
                    disabledContainerColor
                },
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                enabled = enabled
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = OnmoimTheme.typography.body2SemiBold.copy(
                color = Color.White
            )
        )
    }
}