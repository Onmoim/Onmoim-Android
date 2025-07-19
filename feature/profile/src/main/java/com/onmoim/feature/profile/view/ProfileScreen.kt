package com.onmoim.feature.profile.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.onmoim.core.designsystem.component.CommonChip
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.shimmerBackground
import com.onmoim.feature.profile.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProfileRoute(
    bottomBar: @Composable () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileScreen(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            onClickSetting = {},
            onClickNotificationSetting = {},
            onClickWithdrawal = {}
        )
        bottomBar()
    }
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    onClickSetting: () -> Unit,
    onClickNotificationSetting: () -> Unit,
    onClickWithdrawal: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        ProfileAppBar(
            onClickSetting = onClickSetting
        )
        Spacer(Modifier.height(16.dp))
        ProfileCard(
            modifier = Modifier.padding(horizontal = 15.dp),
            profileImgUrl = "https://picsum.photos/200",
            userName = "홍길동",
            location = "서울",
            birthDate = LocalDate.of(2000, 1, 1),
            interestCategories = List(5) { "카테고리${it + 1}" }
        )
        Spacer(Modifier.height(8.dp))
        MyGroupStatus(
            modifier = Modifier.fillMaxWidth(),
            favoriteGroupCount = 1,
            recentGroupCount = 2,
            joinGroupCount = 3
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickNotificationSetting
                )
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_alarm),
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.profile_notification_setting),
                    style = OnmoimTheme.typography.caption1Regular.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null
            )
        }
        Text(
            text = stringResource(R.string.profile_withdrawal),
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickWithdrawal
                ),
            style = OnmoimTheme.typography.caption1Regular.copy(
                color = OnmoimTheme.colors.gray05,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}

@Composable
private fun ProfileAppBar(
    onClickSetting: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 7.dp,
                bottom = 7.dp,
                start = 16.dp,
                end = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.profile),
            style = OnmoimTheme.typography.title3Bold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        NavigationIconButton(
            onClick = onClickSetting
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_setting),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ProfileCard(
    modifier: Modifier = Modifier,
    profileImgUrl: String?,
    userName: String,
    location: String,
    birthDate: LocalDate,
    interestCategories: List<String>,
) {
    val profilePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).apply {
            data(profileImgUrl)
        }.build()
    )
    val profilePainterState by profilePainter.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        Row {
            Crossfade(
                targetState = profilePainterState,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            ) { state ->
                when (state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmerBackground()
                        )
                    }

                    is AsyncImagePainter.State.Success -> {
                        Image(
                            painter = state.painter,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(R.drawable.ic_user_80),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .weight(1f)
            ) {
                Text(
                    text = userName,
                    style = OnmoimTheme.typography.body1SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "$location・${
                        DateTimeFormatter.ofPattern("yyyy. M. D").format(birthDate)
                    }",
                    style = OnmoimTheme.typography.caption1Regular.copy(
                        color = OnmoimTheme.colors.gray05
                    )
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            interestCategories.forEach {
                CommonChip(
                    label = it,
                    textColor = OnmoimTheme.colors.gray05,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}

@Composable
private fun MyGroupStatus(
    modifier: Modifier = Modifier,
    favoriteGroupCount: Int,
    recentGroupCount: Int,
    joinGroupCount: Int
) {
    Column(
        modifier = modifier
    ) {
        HorizontalDivider(
            thickness = 5.dp,
            color = OnmoimTheme.colors.gray01
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp)
        ) {
            val groupStatusItemModifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .weight(1f)
            val groupStatusItemVerticalArrangement = Arrangement.spacedBy(8.dp)
            val groupStatusItemHorizontalArrangement = Alignment.CenterHorizontally
            val groupStatusItemCountTextStyle = OnmoimTheme.typography.body1SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
            val groupStatusItemLabelTextStyle = OnmoimTheme.typography.caption2Regular.copy(
                color = OnmoimTheme.colors.gray05
            )

            Column(
                modifier = groupStatusItemModifier,
                verticalArrangement = groupStatusItemVerticalArrangement,
                horizontalAlignment = groupStatusItemHorizontalArrangement
            ) {
                Text(
                    text = favoriteGroupCount.toString(),
                    style = groupStatusItemCountTextStyle
                )
                Text(
                    text = stringResource(R.string.profile_favorit_group),
                    style = groupStatusItemLabelTextStyle
                )
            }
            Column(
                modifier = groupStatusItemModifier,
                verticalArrangement = groupStatusItemVerticalArrangement,
                horizontalAlignment = groupStatusItemHorizontalArrangement
            ) {
                Text(
                    text = recentGroupCount.toString(),
                    style = groupStatusItemCountTextStyle
                )
                Text(
                    text = stringResource(R.string.profile_recent_group),
                    style = groupStatusItemLabelTextStyle
                )
            }
            Column(
                modifier = groupStatusItemModifier,
                verticalArrangement = groupStatusItemVerticalArrangement,
                horizontalAlignment = groupStatusItemHorizontalArrangement
            ) {
                Text(
                    text = joinGroupCount.toString(),
                    style = groupStatusItemCountTextStyle
                )
                Text(
                    text = stringResource(R.string.profile_join_group),
                    style = groupStatusItemLabelTextStyle
                )
            }
        }
        HorizontalDivider(
            thickness = 5.dp,
            color = OnmoimTheme.colors.gray01
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    OnmoimTheme {
        ProfileScreen(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxSize(),
            onClickSetting = {},
            onClickNotificationSetting = {},
            onClickWithdrawal = {}
        )
    }
}