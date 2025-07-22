package com.onmoim.feature.profile.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonSwitch
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.profile.R

@Composable
fun NotificationSettingRoute(

) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    NotificationSettingScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        }
    )
}

@Composable
private fun NotificationSettingScreen(
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.profile_notification_setting),
                    style = OnmoimTheme.typography.body1SemiBold
                )
            },
            navigationIcon = {
                NavigationIconButton(
                    onClick = onBack
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            }
        )
        Text(
            text = stringResource(R.string.profile_notification_setting_schedule),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = OnmoimTheme.typography.body2SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        LabelSwitchItem(
            label = stringResource(R.string.profile_notification_setting_schedule_a_days_notice),
            checked = true,
            onCheckedChange = { }
        )
        HorizontalDivider(
            thickness = 5.dp,
            color = OnmoimTheme.colors.gray01
        )
        Text(
            text = stringResource(R.string.profile_notification_setting_board),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = OnmoimTheme.typography.body2SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        LabelSwitchItem(
            label = stringResource(R.string.profile_notification_setting_comments_on_my_post),
            checked = false,
            onCheckedChange = { }
        )
        LabelSwitchItem(
            label = stringResource(R.string.profile_notification_setting_empathy_in_my_post),
            checked = false,
            onCheckedChange = { }
        )
        HorizontalDivider(
            thickness = 5.dp,
            color = OnmoimTheme.colors.gray01
        )
        Text(
            text = stringResource(R.string.profile_notification_setting_group_chat),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = OnmoimTheme.typography.body2SemiBold.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        // TODO: api 연동하면 모임 채팅 알림 설정 추가
    }
}

@Composable
private fun LabelSwitchItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val dividerColor = OnmoimTheme.colors.gray02

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = dividerColor,
                    strokeWidth = 0.5.dp.toPx(),
                    start = Offset(15.dp.toPx(), size.height),
                    end = Offset(size.width - 15.dp.toPx(), size.height)
                )
            }
            .padding(horizontal = 15.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = OnmoimTheme.typography.caption1Regular.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        CommonSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview
@Composable
private fun NotificationSettingScreenPreview() {
    OnmoimTheme {
        NotificationSettingScreen(
            onBack = {}
        )
    }
}