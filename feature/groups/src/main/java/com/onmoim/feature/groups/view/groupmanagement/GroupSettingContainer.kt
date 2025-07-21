package com.onmoim.feature.groups.view.groupmanagement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.component.CommonListItem
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R

@Composable
fun GroupSettingContainer(
    modifier: Modifier = Modifier,
    onClickGroupEdit: () -> Unit,
    onClickScheduleManagement: () -> Unit,
    onClickCreateMeet: () -> Unit,
    onClickWriteNotice: () -> Unit,
    onClickGroupDelete: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        CommonListItem(
            onClick = onClickGroupEdit,
            title = stringResource(R.string.group_management_group_info_edit),
            trailing = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 15.dp),
            thickness = 0.5.dp,
            color = OnmoimTheme.colors.gray02
        )
        CommonListItem(
            onClick = onClickScheduleManagement,
            title = stringResource(R.string.group_management_schedule_management),
            trailing = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 15.dp),
            thickness = 0.5.dp,
            color = OnmoimTheme.colors.gray02
        )
        CommonListItem(
            onClick = onClickCreateMeet,
            title = stringResource(R.string.group_management_create_meet),
            trailing = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 15.dp),
            thickness = 0.5.dp,
            color = OnmoimTheme.colors.gray02
        )
        CommonListItem(
            onClick = onClickWriteNotice,
            title = stringResource(R.string.group_management_write_notice),
            trailing = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 15.dp),
            thickness = 0.5.dp,
            color = OnmoimTheme.colors.gray02
        )
        Box(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClickGroupDelete
                )
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(R.string.group_delete),
                style = OnmoimTheme.typography.caption1Regular.copy(
                    color = OnmoimTheme.colors.gray05,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}