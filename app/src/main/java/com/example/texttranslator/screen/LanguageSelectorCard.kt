package com.example.texttranslator.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.texttranslator.R


@Composable
fun LanguageSelectorCard(
    lang1: String,
    lang2: String,
    onLang1Click: () -> Unit,
    onLang2Click: () -> Unit,
    onSwapClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(73.dp)
            .padding(top = 15.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(7.dp),
                onClick = onLang1Click
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE7F3FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = lang1, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.rotate_icon),
                contentDescription = "Swap Languages",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onSwapClick() }
            )

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(7.dp),
                onClick = onLang2Click
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE7F3FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = lang2, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}
