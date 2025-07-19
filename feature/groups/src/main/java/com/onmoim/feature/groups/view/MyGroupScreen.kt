package com.onmoim.feature.groups.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.onmoim.core.designsystem.theme.OnmoimTheme

@Composable
fun MyGroupRoute(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        topBar()
        MyGroupScreen(
            modifier = Modifier.weight(1f)
        )
        bottomBar()
    }
}

@Composable
private fun MyGroupScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {

    }
}

@Preview
@Composable
private fun MyGroupScreenPreview() {
    OnmoimTheme {
        MyGroupScreen(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxSize()
        )
    }
}