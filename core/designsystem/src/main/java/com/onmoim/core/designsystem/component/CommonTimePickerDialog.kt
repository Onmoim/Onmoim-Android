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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.theme.OnmoimTheme
import java.time.LocalTime

enum class TwelveHourPeriod(
    val labelId: Int
) {
    AM(R.string.am), PM(R.string.pm)
}

@Composable
fun CommonTimePickerDialog(
    onDismissRequest: () -> Unit,
    initialTime: LocalTime = LocalTime.now(),
    minTime: LocalTime = LocalTime.MIN,
    onClickConfirm: (LocalTime) -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    var selectedTwelveHourPeriod by remember {
        val period = if (initialTime.hour < 12) {
            TwelveHourPeriod.AM
        } else {
            TwelveHourPeriod.PM
        }
        mutableStateOf(period)
    }
    var selectedHour by remember {
        val hour = if (selectedTwelveHourPeriod == TwelveHourPeriod.AM) {
            if (initialTime.hour == 0) 12 else initialTime.hour
        } else {
            if (initialTime.hour == 12) 12 else initialTime.hour - 12
        }
        mutableIntStateOf(hour)
    }
    var selectedMinute by remember { mutableIntStateOf(initialTime.minute) }
    val context = LocalContext.current

    val availableHours = remember(selectedTwelveHourPeriod, minTime) {
        (1..12).filter { h ->
            val hour24 = when (selectedTwelveHourPeriod) {
                TwelveHourPeriod.AM -> if (h == 12) 0 else h
                TwelveHourPeriod.PM -> if (h == 12) 12 else h + 12
            }
            hour24 >= minTime.hour
        }
    }

    val availableMinutes = remember(selectedTwelveHourPeriod, selectedHour, minTime) {
        val hour24 = when (selectedTwelveHourPeriod) {
            TwelveHourPeriod.AM -> if (selectedHour == 12) 0 else selectedHour
            TwelveHourPeriod.PM -> if (selectedHour == 12) 12 else selectedHour + 12
        }
        val minMinute = if (hour24 == minTime.hour) {
            minTime.minute
        } else {
            0
        }
        (minMinute..59).toList()
    }

    LaunchedEffect(selectedTwelveHourPeriod) {
        if (selectedHour !in availableHours) {
            selectedHour = availableHours.first()
        }
    }

    LaunchedEffect(selectedHour) {
        if (selectedMinute !in availableMinutes) {
            selectedMinute = availableMinutes.first()
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
                                text = stringResource(R.string.common_time_picker_dialog),
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
                                items = TwelveHourPeriod.entries.filter {
                                    if (minTime.hour >= 12) {
                                        it == TwelveHourPeriod.PM
                                    } else {
                                        true
                                    }
                                }.map {
                                    when (it) {
                                        TwelveHourPeriod.AM -> stringResource(R.string.am)
                                        TwelveHourPeriod.PM -> stringResource(R.string.pm)
                                    }
                                },
                                initialValue = stringResource(selectedTwelveHourPeriod.labelId),
                                onValueChange = { value ->
                                    TwelveHourPeriod.entries.find { context.getString(it.labelId) == value }
                                        ?.let {
                                            selectedTwelveHourPeriod = it
                                        }
                                }
                            )
                            Picker(
                                items = availableHours.map { it.toString() },
                                initialValue = selectedHour.toString(),
                                onValueChange = { selectedHour = it.toInt() }
                            )
                            Picker(
                                items = availableMinutes.map { it.toString() },
                                initialValue = selectedMinute.toString(),
                                onValueChange = { selectedMinute = it.toInt() }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .clickable {
                            val hour = if (selectedTwelveHourPeriod == TwelveHourPeriod.AM) {
                                if (selectedHour == 12) 0 else selectedHour
                            } else {
                                if (selectedHour == 12) 12 else selectedHour + 12
                            }
                            onClickConfirm(LocalTime.of(hour, selectedMinute))
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
private fun CommonTimePickerPreview() {
    OnmoimTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonTimePickerDialog(
                onDismissRequest = {},
                onClickConfirm = {}
            )
        }
    }
}