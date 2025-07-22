package com.onmoim.feature.groups.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.CommonButton
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.advancedShadow
import com.onmoim.feature.groups.R

@Composable
fun GroupOpenCompleteRoute(
    onNavigateToGroupDetail: () -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    GroupOpenCompleteScreen(
        onBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        onClickGroupHome = onNavigateToGroupDetail
    )
}

@Composable
private fun GroupOpenCompleteScreen(
    onBack: () -> Unit,
    onClickGroupHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OnmoimTheme.colors.backgroundColor)
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.group_open_complete),
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
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.offset(y = (-90).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .advancedShadow(
                            color = Color(0xFF878787),
                            alpha = 0.2f,
                            cornersRadius = 999.dp,
                            shadowBlurRadius = 15.dp,
                            offsetX = 0.dp,
                            offsetY = 4.5.dp
                        )
                        .background(
                            color = OnmoimTheme.colors.primaryMint,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null
                    )
                }
                Spacer(Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.group_open_complete_title),
                    style = OnmoimTheme.typography.body1SemiBold.copy(
                        color = OnmoimTheme.colors.textColor
                    )
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.group_open_complete_sub_title),
                    style = OnmoimTheme.typography.body2Regular.copy(
                        color = OnmoimTheme.colors.gray04
                    )
                )
                Spacer(Modifier.height(68.dp))
                CommonButton(
                    onClick = onClickGroupHome,
                    label = stringResource(R.string.group_open_complete_go_to_group)
                )
            }
        }
    }
}

@Preview
@Composable
private fun GroupOpenCompleteScreenPreview() {
    OnmoimTheme {
        GroupOpenCompleteScreen(
            onBack = {},
            onClickGroupHome = {}
        )
    }
}