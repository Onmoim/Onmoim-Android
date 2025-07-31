package com.onmoim.core.designsystem.component.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.designsystem.R
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.designsystem.theme.shadow2Block
import com.onmoim.core.ui.shimmerBackground
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

enum class ComingScheduleCardButtonType {
    ATTEND, ATTEND_CANCEL, DELETE
}

@Composable
fun ComingScheduleCard(
    modifier: Modifier = Modifier,
    onClickButton: () -> Unit,
    buttonType: ComingScheduleCardButtonType,
    isLightning: Boolean,
    startDate: LocalDateTime,
    title: String,
    placeName: String,
    cost: Int,
    joinCount: Int,
    capacity: Int,
    imageUrl: String?,
    onClickCard: () -> Unit = {},
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(imageUrl)
        }.build()
    )
    val painterState by painter.state.collectAsStateWithLifecycle()
    val nowDateTime = LocalDateTime.now()

    Box(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClickCard
            )
            .shadow2Block(16.dp)
            .background(
                color = OnmoimTheme.colors.backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                start = 15.dp,
                end = 15.dp,
                top = 15.dp,
                bottom = 16.dp
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(74.dp, 32.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickButton
                )
                .background(
                    color = when (buttonType) {
                        ComingScheduleCardButtonType.ATTEND -> {
                            OnmoimTheme.colors.primaryBlue
                        }

                        ComingScheduleCardButtonType.ATTEND_CANCEL -> {
                            OnmoimTheme.colors.primaryPink
                        }

                        ComingScheduleCardButtonType.DELETE -> {
                            OnmoimTheme.colors.gray05
                        }
                    },
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    id = when (buttonType) {
                        ComingScheduleCardButtonType.ATTEND -> {
                            R.string.coming_schedule_attend
                        }

                        ComingScheduleCardButtonType.ATTEND_CANCEL -> {
                            R.string.coming_schedule_attend_cancel
                        }

                        ComingScheduleCardButtonType.DELETE -> {
                            R.string.delete
                        }
                    }
                ),
                style = OnmoimTheme.typography.body2SemiBold.copy(
                    color = Color.White
                )
            )
        }
        Column(
            modifier = Modifier.padding(top = 7.dp)
        ) {
            Row {
                Text(
                    text = DateTimeFormatter.ofPattern("M/d (E)", Locale.KOREAN)
                        .format(startDate),
                    style = OnmoimTheme.typography.body1SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "D${
                        ChronoUnit.DAYS.between(
                            startDate.toLocalDate(),
                            nowDateTime.toLocalDate()
                        )
                    }",
                    style = OnmoimTheme.typography.body1SemiBold.copy(
                        color = OnmoimTheme.colors.alertRed
                    )
                )
            }
            Spacer(Modifier.height(18.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isLightning) {
                    Image(
                        painter = painterResource(R.drawable.ic_lightning),
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    style = OnmoimTheme.typography.body1Bold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
            }
            Spacer(Modifier.height(16.dp))
            Row {
                Box(
                    modifier = Modifier
                        .size(134.dp, 76.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    when (painterState) {
                        is AsyncImagePainter.State.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmerBackground()
                            )
                        }

                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFA4A4A4))
                            )
                        }
                    }
                }
                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 76.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.coming_schedule_date),
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.gray05
                            )
                        )
                        Text(
                            text = DateTimeFormatter.ofPattern("M/d E a h:mm", Locale.KOREAN)
                                .format(startDate),
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.textColor
                            )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.coming_schedule_location),
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.gray05
                            )
                        )
                        Text(
                            text = placeName,
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.textColor
                            )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.coming_schedule_cost),
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.gray05
                            )
                        )
                        Text(
                            text = stringResource(
                                R.string.coming_schedule_formatted_price,
                                DecimalFormat("#,###").format(cost)
                            ),
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.textColor
                            )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.coming_schedule_attend),
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.gray05
                            )
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = OnmoimTheme.colors.alertRed)) {
                                    append(joinCount.toString())
                                }
                                append("/")
                                append(capacity.toString())
                            },
                            style = OnmoimTheme.typography.caption2Regular.copy(
                                color = OnmoimTheme.colors.textColor,
                                fontFeatureSettings = "tnum"
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ComingScheduleCardPreview() {
    OnmoimTheme {
        ComingScheduleCard(
            modifier = Modifier.width(330.dp),
            onClickButton = {},
            buttonType = ComingScheduleCardButtonType.ATTEND,
            isLightning = false,
            startDate = LocalDateTime.now().plusDays(2),
            title = "퇴근 후 독서 정모: 각자 독서",
            placeName = "카페 언노운",
            cost = 1000,
            joinCount = 6,
            capacity = 8,
            imageUrl = "https://picsum.photos/200"
        )
    }
}

@Preview
@Composable
private fun ComingScheduleCardForLightningPreview() {
    OnmoimTheme {
        ComingScheduleCard(
            modifier = Modifier.width(330.dp),
            onClickButton = {},
            buttonType = ComingScheduleCardButtonType.ATTEND_CANCEL,
            isLightning = true,
            startDate = LocalDateTime.now().plusDays(2),
            title = "퇴근 후 독서 정모: 각자 독서",
            placeName = "카페 언노운",
            cost = 1000,
            joinCount = 6,
            capacity = 8,
            imageUrl = "https://picsum.photos/200"
        )
    }
}

@Preview
@Composable
private fun ComingScheduleCardForDeletePreview() {
    OnmoimTheme {
        ComingScheduleCard(
            modifier = Modifier.width(330.dp),
            onClickButton = {},
            buttonType = ComingScheduleCardButtonType.DELETE,
            isLightning = true,
            startDate = LocalDateTime.now().plusDays(2),
            title = "퇴근 후 독서 정모: 각자 독서",
            placeName = "카페 언노운",
            cost = 1000,
            joinCount = 6,
            capacity = 8,
            imageUrl = "https://picsum.photos/200"
        )
    }
}