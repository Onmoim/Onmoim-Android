package com.onmoim.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.R

@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textColor: Color = OnmoimTheme.colors.textColor,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    innerFieldAlignment: Alignment = Alignment.CenterStart
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .heightIn(min = 37.dp)
            .then(modifier),
        enabled = enabled,
        textStyle = OnmoimTheme.typography.body2Regular.copy(
            color = textColor
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .background(
                    color = OnmoimTheme.colors.gray01,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.5.dp
                )
        ) {
            Row(
                modifier = Modifier.align(innerFieldAlignment),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(Modifier.width(8.dp))
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty() && placeholder != null) {
                        placeholder()
                    }
                    innerTextField()
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommonTextFieldPreview() {
    var value by remember { mutableStateOf("") }

    OnmoimTheme {
        CommonTextField(
            value = value,
            onValueChange = {
                value = it
            },
            modifier = Modifier.width(300.dp),
            placeholder = {
                Text(
                    text = "hint",
                    style = OnmoimTheme.typography.body2Regular.copy(
                        color = OnmoimTheme.colors.gray04
                    )
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = OnmoimTheme.colors.gray04
                )
            }
        )
    }
}