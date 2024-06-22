package com.fredy.askquestions.features.ui.screens.chatScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageEvent
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageState
import timber.log.Timber

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    state: MessageState,
    onEvent: (MessageEvent) -> Unit,
) {
    Timber.e("MessageScreen ${state.messages}")
    val onSend: () -> Unit = {
        if (state.inputText.isNotBlank()) {
            onEvent(MessageEvent.OnSendClick)
        }
    }
    val lazyListState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Messages display area

        LaunchedEffect(state.messages) {
            if (state.messages.isNotEmpty()) lazyListState.animateScrollToItem(
                state.messages.lastIndex
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(
                    1f
                ),
            state = lazyListState,
        ) {
            items(
                state.messages,
                key = { it.message.messageId }) { messageMap ->
                MessageBubble(
                    text = messageMap.message.text,
                    isUser = messageMap.isUser
                )
            }

        }
        // Input field and send button with animation
        val visibleState = state.inputText.isNotBlank()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .fillMaxWidth()
        ) {
            // Input field
            OutlinedTextField(
                value = state.inputText,
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
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        end = 8.dp
                    )
            )

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
                        onSend()
                    }, modifier = Modifier.align(
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