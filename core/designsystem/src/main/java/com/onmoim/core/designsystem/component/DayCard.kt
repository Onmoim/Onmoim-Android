package com.onmoim.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.designsystem.theme.pretendard
import com.onmoim.core.designsystem.theme.shadow1
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean,
    date: LocalDate
) {
    Box(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .size(55.dp, 68.dp)
            .shadow1(20.dp)
            .background(
                color = if (selected) {
                    OnmoimTheme.colors.primaryBlue
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                fontFamily = pretendard,
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                color = if (selected) {
                    Color.White
                } else {
                    OnmoimTheme.colors.textColor
                }
            )
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                fontFamily = pretendard,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 22.sp,
                color = if (selected) {
                    Color.White
                } else {
                    OnmoimTheme.colors.gray05
                }
            )
        }
    }
}

@Preview
@Composable
private fun DayCardPreview1() {
    OnmoimTheme {
        DayCard(
            onClick = {},
            selected = false,
            date = LocalDate.now()
        )
    }
}

@Preview
@Composable
private fun DayCardPreview2() {
    OnmoimTheme {
        DayCard(
            onClick = {},
            selected = true,
            date = LocalDate.now()
        )
    }
}