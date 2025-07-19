package com.onmoim.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.designsystem.theme.shadow2Block
import com.onmoim.core.ui.animateAlignmentAsState

@Composable
fun CommonSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    checkedTrackColor: Color = OnmoimTheme.colors.primaryBlue,
    uncheckedTrackColor: Color = OnmoimTheme.colors.gray03
) {
    val width = 53.dp
    val height = 32.dp
    val trackShape = RoundedCornerShape(20.dp)
    val gap = 4.dp
    val animDuration = 300
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val animateAlignment by animateAlignmentAsState(
        targetBiasValue = if (checked) 1f else -1f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        ),
        label = "animateAlignment"
    )
    val animateColor by animateColorAsState(
        targetValue = if (checked) checkedTrackColor else uncheckedTrackColor,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        ),
        label = "animateColor"
    )

    Box(
        modifier = Modifier
            .size(width, height)
            .background(
                color = animateColor,
                shape = trackShape
            )
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                onCheckedChange(!checked)
            },
        contentAlignment = animateAlignment
    ) {
        Box(
            Modifier
                .size(height)
                .padding(gap)
                .shadow2Block(999.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
        )
    }
}

@Preview
@Composable
private fun CustomSwitchPreview() {
    var switch by remember { mutableStateOf(false) }

    OnmoimTheme {
        CommonSwitch(
            checked = switch,
            onCheckedChange = {
                switch = it
            }
        )
    }
}