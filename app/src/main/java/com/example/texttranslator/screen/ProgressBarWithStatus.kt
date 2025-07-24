package com.example.texttranslator.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressBarWithStatus(progress: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress Percentage Text
        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Linear Progress Bar
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
    }
}
@Composable
fun ProgressScreen(
    progress: Float,
    onProgressComplete: () -> Unit,
    modifier: Modifier
) {
    LaunchedEffect(progress) {
        if (progress >= 1f) {
            onProgressComplete()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = "${(progress * 100).toInt()}%", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
    }
}

