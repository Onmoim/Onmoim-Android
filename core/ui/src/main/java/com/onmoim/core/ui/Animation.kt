package com.onmoim.core.ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment

@Composable
fun animateAlignmentAsState(
    targetBiasValue: Float,
    animationSpec: AnimationSpec<Float> = spring(),
    visibilityThreshold: Float = 0.01f,
    label: String,
    finishedListener: ((Float) -> Unit)? = null
): State<BiasAlignment> {
    val bias by animateFloatAsState(
        targetValue = targetBiasValue,
        animationSpec = animationSpec,
        visibilityThreshold = visibilityThreshold,
        label = label,
        finishedListener = finishedListener
    )
    return remember {
        derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
    }
}