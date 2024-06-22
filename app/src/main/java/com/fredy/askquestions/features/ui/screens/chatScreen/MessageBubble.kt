package com.fredy.askquestions.features.ui.screens.chatScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MessageBubble(text: String, isUser: Boolean) {
    val backgroundColor = if (isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.LightGray
    }

    val contentAlignment = if (isUser) {
        Alignment.TopEnd
    } else {
        Alignment.TopStart
    }

    Box(
        modifier = Modifier.padding(vertical = 4.dp).padding(
                horizontal = 16.dp
            ).fillMaxWidth(),
        contentAlignment = contentAlignment
    ) {
        Box(
            modifier = Modifier.background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                ).padding(8.dp).clip(
                    RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                text = text,
                color = if (isUser) Color.White else Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}