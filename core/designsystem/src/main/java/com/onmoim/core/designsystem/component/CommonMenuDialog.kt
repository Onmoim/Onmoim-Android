package com.onmoim.core.designsystem.component

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.R

@Composable
fun CommonMenuDialog(
    onDismissRequest: () -> Unit,
    onClickCancel: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable (ColumnScope.() -> Unit)
) {
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
            Column {
                Column(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(RoundedCornerShape(15.dp)),
                    content = content
                )
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .clickable {
                            onClickCancel()
                        }
                        .background(
                            color = OnmoimTheme.colors.primaryBlue,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clip(RoundedCornerShape(15.dp))
                        .fillMaxWidth()
                        .height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = OnmoimTheme.typography.body1SemiBold.copy(
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CommonMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    includeDivider: Boolean = true
) {
    Column(
        modifier = modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(vertical = 14.dp),
            style = OnmoimTheme.typography.body2Regular.copy(
                color = OnmoimTheme.colors.textColor
            )
        )
        if (includeDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = OnmoimTheme.colors.gray03
            )
        }
    }
}

@Preview
@Composable
private fun CommonMenuDialogPreview() {
    OnmoimTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonMenuDialog(
                onDismissRequest = {},
                onClickCancel = {}
            ) {
                List(2) {
                    CommonMenuItem(
                        onClick = {},
                        label = "메뉴$it",
                        includeDivider = it < 1
                    )
                }
            }
        }
    }
}