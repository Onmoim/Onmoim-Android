package com.onmoim.feature.mymeet.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.onmoim.core.ui.theme.OnmoimTheme

@Composable
fun MyMeetRoute(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        topBar()
        MyMeetScreen(
            modifier = Modifier.weight(1f)
        )
        bottomBar()
    }
}

@Composable
private fun MyMeetScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {

    }
}

@Preview
@Composable
private fun MyMeetScreenPreview() {
    OnmoimTheme {
        MyMeetScreen(
            modifier = Modifier
                .background(OnmoimTheme.colors.backgroundColor)
                .fillMaxSize()
        )
    }
}