package com.fredy.chat.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.fredy.chat.viewmodels.MessageEvent

@Composable
fun MessageTextField(
    modifier: Modifier = Modifier,
    inputText: String,
    onEvent: (MessageEvent) -> Unit,
    visibleState: Boolean
) {
    val onSend: () -> Unit = {
        if (inputText.isNotBlank()) {
            onEvent(MessageEvent.OnSendClick)
        }
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier.padding(
            horizontal = 16.dp, vertical = 8.dp
        ).fillMaxWidth()
    ) {
        // Input field
        OutlinedTextField(
            value = inputText,
            onValueChange = {
                onEvent(
                    MessageEvent.OnTextChange(
                        it
                    )
                )
            },
            label = { Text("Type your message") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onSend()
                },
            ),
            modifier = Modifier.weight(1f).padding(
                end = 8.dp
            ),
            shape = MaterialTheme.shapes.large,
            maxLines = 6
        )

        AnimatedVisibility(
            visible = visibleState,
        ) {
            IconButton(onClick = {
                onSend()
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

    }
}
