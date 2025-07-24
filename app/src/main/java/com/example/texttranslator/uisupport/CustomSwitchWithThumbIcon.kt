package com.example.texttranslator.uisupport

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.texttranslator.R
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image


@Composable
fun CustomSwitch(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (isChecked) 57.dp else 4.dp, // adjust based on width
        animationSpec = tween(300)
    )

    // Track
    Box(
        modifier = modifier
            .width(97.dp)
            .height(35.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isChecked) Color(0xFF2890FF) else Color(0xFFDE6240) // track colors
            )
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Thumb
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isChecked) Color(0xFF2890FF) else Color(0xFFF44336)
                )
                .border(
                    width = 3.dp,
                    color = if (isChecked) Color(0xFF2890FF) else Color(0xFFDE6240),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(
                    id = if (isChecked) R.drawable.switch_icon_on else R.drawable.switch_icon_off
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomSwitchPreview() {
    var isChecked by remember { mutableStateOf(false) }
    CustomSwitch(
        isChecked = isChecked,
        onCheckedChange = { isChecked = it }
    )
}




/*@Preview(showSystemUi = true)
@Composable
fun CustomSwitchWithThumbIcon() {
    var isChecked by remember { mutableStateOf(false) }
    val thumbOffset by animateDpAsState(
        targetValue = if (isChecked) 52.dp else 4.dp,
        animationSpec = tween(300)
    )

    Box(
        modifier = Modifier
            .width(97.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isChecked) Color(0xFF2890FF) else Color(0xFFF44336))
            .clickable { isChecked = !isChecked }
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isChecked) Color(0xFF2890FF) else Color(0xFFF44336))
                .border(
                    width = 3.dp,
                    color = if (isChecked) Color(0xFF2890FF) else Color(0xFFDE6240),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    id = if (isChecked) R.drawable.switch_icon_on else R.drawable.switch_icon_off
                ),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}*/
