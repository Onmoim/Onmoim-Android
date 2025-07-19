package com.onmoim.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.constant.Gender
import com.onmoim.core.designsystem.theme.OnmoimTheme

@Composable
fun GenderToggle(
    value: Gender?,
    onValueChange: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .border(
                width = 0.5.dp,
                color = OnmoimTheme.colors.gray04,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Gender.entries.forEach {
            Box(
                modifier = Modifier
                    .clickable {
                        onValueChange(it)
                    }
                    .weight(1f)
                    .heightIn(min = 38.dp)
                    .padding(
                        start = when (it) {
                            Gender.MALE -> 0.dp
                            Gender.FEMALE -> 15.dp
                        },
                        end = when (it) {
                            Gender.MALE -> 15.dp
                            Gender.FEMALE -> 0.dp
                        }
                    ),
                contentAlignment = when (it) {
                    Gender.MALE -> Alignment.CenterEnd
                    Gender.FEMALE -> Alignment.CenterStart
                }
            ) {
                Text(
                    text = when (it) {
                        Gender.MALE -> stringResource(R.string.male)
                        Gender.FEMALE -> stringResource(R.string.female)
                    },
                    style = OnmoimTheme.typography.body2Regular.copy(
                        color = if (value == it) {
                            OnmoimTheme.colors.textColor
                        } else {
                            OnmoimTheme.colors.gray04
                        }
                    )
                )
            }
        }
    }
}