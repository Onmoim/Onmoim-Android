package com.onmoim.core.designsystem.component

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.R
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CommonDatePickerDialog(
    onDismissRequest: () -> Unit,
    initialDate: LocalDate = LocalDate.now(),
    minDate: LocalDate = LocalDate.of(1900, 1, 1),
    maxDate: LocalDate = LocalDate.now(),
    onClickConfirm: (LocalDate) -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    val effectiveInitialDate = remember(initialDate, minDate, maxDate) {
        initialDate.coerceIn(minDate, maxDate)
    }

    var selectedYear by remember { mutableIntStateOf(effectiveInitialDate.year) }
    var selectedMonth by remember { mutableIntStateOf(effectiveInitialDate.monthValue) }
    var selectedDay by remember { mutableIntStateOf(effectiveInitialDate.dayOfMonth) }

    val availableYears = remember(minDate, maxDate) {
        (minDate.year..maxDate.year).toList()
    }
    val availableMonths = remember(selectedYear, minDate, maxDate) {
        val startMonth = if (selectedYear == minDate.year) minDate.monthValue else 1
        val endMonth = if (selectedYear == maxDate.year) maxDate.monthValue else 12
        (startMonth..endMonth).toList()
    }
    val availableDays = remember(selectedYear, selectedMonth, minDate, maxDate) {
        val startDay =
            if (selectedYear == minDate.year && selectedMonth == minDate.monthValue) minDate.dayOfMonth else 1
        val endDay =
            if (selectedYear == maxDate.year && selectedMonth == maxDate.monthValue) maxDate.dayOfMonth else YearMonth.of(
                selectedYear,
                selectedMonth
            ).lengthOfMonth()
        (startDay..endDay).toList()
    }

    LaunchedEffect(selectedYear, availableMonths) {
        if (selectedMonth !in availableMonths) {
            selectedMonth = availableMonths.lastOrNull() ?: 1
        }
    }

    LaunchedEffect(selectedYear, selectedMonth, availableDays) {
        if (selectedDay !in availableDays) {
            selectedDay = availableDays.lastOrNull() ?: 1
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)

        Surface(
            modifier = Modifier
                .padding(bottom = 48.dp)
                .widthIn(max = 330.dp),
            color = Color.Transparent
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .heightIn(min = 221.dp)
                        .background(
                            color = OnmoimTheme.colors.backgroundColor,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(RoundedCornerShape(15.dp))
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .padding(start = 20.dp, top = 8.5.dp, end = 8.5.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.date_select),
                                style = OnmoimTheme.typography.body1SemiBold.copy(
                                    color = OnmoimTheme.colors.textColor
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        indication = ripple(
                                            bounded = false,
                                            radius = 20.dp
                                        ),
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = onDismissRequest
                                    )
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_close_17),
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Picker(
                                items = availableYears.map { it.toString() },
                                initialValue = selectedYear.toString(),
                                onValueChange = { selectedYear = it.toInt() }
                            )
                            Picker(
                                items = availableMonths.map { it.toString() },
                                initialValue = selectedMonth.toString(),
                                onValueChange = { selectedMonth = it.toInt() }
                            )
                            Picker(
                                items = availableDays.map { it.toString() },
                                initialValue = selectedDay.toString(),
                                onValueChange = { selectedDay = it.toInt() }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .clickable {
                            onClickConfirm(LocalDate.of(selectedYear, selectedMonth, selectedDay))
                        }
                        .background(
                            color = OnmoimTheme.colors.primaryBlue,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(RoundedCornerShape(15.dp))
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = OnmoimTheme.typography.body1SemiBold.copy(
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommonDatePickerPreview() {
    OnmoimTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonDatePickerDialog(
                onDismissRequest = {},
                onClickConfirm = {}
            )
        }
    }
}