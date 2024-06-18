package com.fredy.askquestions.features.ui.screens.chatScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.ChatState
import com.fredy.askquestions.features.domain.models.Message

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    state: ChatState,
    onTextChange: (String) -> Unit,
    onClick: () -> Unit,
    messages: List<Message>
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Messages display area
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(messages) { message ->
                MessageBubble(
                    text = message.text,
                    isUser = message.senderId == state.currentUserId
                )
            }
        }

        // Spacer to push input area to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Input field and send button with animation
        val visibleState = state.inputText.isNotBlank()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ).fillMaxWidth()
        ) {
            // Input field
            OutlinedTextField(value = state.inputText,
                onValueChange = { onTextChange(it) },
                label = { Text("Type your message") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (state.inputText.isNotBlank()) {
                            onClick()
                        }
                    }),
                modifier = Modifier.weight(1f).padding(
                        end = 8.dp
                    ))

            // Send button with animation
            AnimatedVisibility(
                visible = visibleState,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
            ) {
                IconButton(
                    onClick = {
                        if (state.inputText.isNotBlank()) {
                            onClick()
                        }
                    },
                    modifier = Modifier.align(
                        Alignment.CenterVertically
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}