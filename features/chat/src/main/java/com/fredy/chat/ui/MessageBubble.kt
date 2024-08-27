package com.fredy.chat.ui

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MessageBubble(
    modifier: Modifier = Modifier,
    rightColor: Color = MaterialTheme.colorScheme.primary,
    leftColor: Color = MaterialTheme.colorScheme.secondary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    bubbleEndPadding: Dp = 16.dp,
    text: String,
    isRight: Boolean
) {
    val backgroundColor = if (isRight) {
        rightColor
    } else {
        leftColor
    }

    val contentAlignment = if (isRight) {
        Alignment.TopEnd
    } else {
        Alignment.TopStart
    }

    Box(
        modifier = modifier
            .padding(vertical = 4.dp)
            .padding(
                horizontal = 16.dp
            )
            .then(
                if (isRight) {
                    Modifier.padding(start = bubbleEndPadding)
                } else {
                    Modifier.padding(end = bubbleEndPadding)
                }
            )
            .fillMaxWidth(),
        contentAlignment = contentAlignment
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .clip(
                    RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                text = text,
                color = textColor,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
