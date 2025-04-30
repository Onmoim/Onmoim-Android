package com.onmoim.core.ui.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        val formattedText = buildString {
            for (i in originalText.indices) {
                append(originalText[i])
                if (i == 3 || i == 5) {
                    if (i < originalText.length - 1) {
                        append('.')
                    }
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 4 -> offset
                    offset <= 6 -> offset + 1
                    else -> offset + 2
                }.coerceAtMost(formattedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 4 -> offset
                    offset <= 7 -> offset - 1
                    else -> offset - 2
                }.coerceAtMost(originalText.length)
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}