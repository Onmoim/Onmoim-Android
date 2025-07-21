package com.onmoim.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.R

@Composable
private fun BaseDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable BoxScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Box(
            modifier = Modifier
                .width(280.dp)
                .background(
                    color = OnmoimTheme.colors.backgroundColor,
                    shape = RoundedCornerShape(15.dp)
                )
                .clip(RoundedCornerShape(15.dp)),
            content = content
        )
    }
}

@Composable
fun CommonDialog(
    onDismissRequest: () -> Unit,
    content: String,
    onClickConfirm: () -> Unit,
    onClickDismiss: (() -> Unit)? = null,
    title: String = stringResource(R.string.guide),
    confirmText: String = stringResource(R.string.confirm),
    dismissText: String = stringResource(R.string.cancel),
    properties: DialogProperties = DialogProperties()
) {
    BaseDialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 40.dp, end = 40.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    style = OnmoimTheme.typography.body2SemiBold.copy(
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = content,
                    modifier = Modifier.fillMaxWidth(),
                    style = OnmoimTheme.typography.body2Regular.copy(
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(24.dp))
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = OnmoimTheme.colors.gray03
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                if (onClickDismiss != null) {
                    Box(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = onClickDismiss
                            )
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dismissText,
                            modifier = Modifier.padding(vertical = 16.dp),
                            style = OnmoimTheme.typography.body2SemiBold.copy(
                                color = OnmoimTheme.colors.alertRed
                            )
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = OnmoimTheme.colors.gray03
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickConfirm
                        )
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = confirmText,
                        modifier = Modifier.padding(vertical = 16.dp),
                        style = OnmoimTheme.typography.body2SemiBold.copy(
                            color = OnmoimTheme.colors.primaryBlue
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CommonDialog(
    onDismissRequest: () -> Unit,
    onClickConfirm: () -> Unit,
    onClickDismiss: (() -> Unit)? = null,
    title: String = stringResource(R.string.guide),
    confirmText: String = stringResource(R.string.confirm),
    dismissText: String = stringResource(R.string.cancel),
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    BaseDialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    style = OnmoimTheme.typography.body2SemiBold.copy(
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(16.dp))
                content()
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = OnmoimTheme.colors.gray03
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                if (onClickDismiss != null) {
                    Box(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = onClickDismiss
                            )
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dismissText,
                            modifier = Modifier.padding(vertical = 16.dp),
                            style = OnmoimTheme.typography.body2SemiBold.copy(
                                color = OnmoimTheme.colors.alertRed
                            )
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = OnmoimTheme.colors.gray03
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onClickConfirm
                        )
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = confirmText,
                        modifier = Modifier.padding(vertical = 16.dp),
                        style = OnmoimTheme.typography.body2SemiBold.copy(
                            color = OnmoimTheme.colors.primaryBlue
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommonDialogOneButtonPreview() {
    OnmoimTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonDialog(
                onDismissRequest = {},
                content = "내용",
                onClickConfirm = {}
            )
        }
    }
}

@Preview
@Composable
private fun CommonDialogTwoButtonPreview() {
    OnmoimTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonDialog(
                onDismissRequest = {},
                onClickConfirm = {},
                onClickDismiss = {},
                title = "내용 입력"
            ) {
                CommonTextField(
                    value = "내용내용",
                    onValueChange = {},
                    modifier = Modifier.height(100.dp),
                    innerFieldAlignment = Alignment.TopStart
                )
            }
        }
    }
}