package com.onmoim.feature.groups.view.meetingplacesearch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.feature.groups.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceInfoModalBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    title: String,
    address: String,
    phoneNumber: String,
    onClickSetting: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = OnmoimTheme.colors.backgroundColor,
        scrimColor = Color.Transparent,
        dragHandle = {
            Box(
                Modifier
                    .padding(vertical = 12.dp)
                    .background(
                        color = OnmoimTheme.colors.gray04,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(40.dp, 4.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = OnmoimTheme.typography.body2Bold.copy(
                    color = OnmoimTheme.colors.textColor
                )
            )
            Text(
                text = address,
                style = OnmoimTheme.typography.caption2Regular.copy(
                    color = OnmoimTheme.colors.gray05
                )
            )
            Text(
                text = phoneNumber,
                style = OnmoimTheme.typography.caption2Regular.copy(
                    color = OnmoimTheme.colors.gray05
                )
            )
            Box(
                modifier = Modifier
                    .clickable {
                        onClickSetting()
                    }
                    .background(
                        color = OnmoimTheme.colors.primaryBlue,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth()
                    .height(33.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.meeting_place_search_btn_set_place),
                    style = OnmoimTheme.typography.body2SemiBold.copy(
                        color = Color.White
                    )
                )
            }
        }
    }
}