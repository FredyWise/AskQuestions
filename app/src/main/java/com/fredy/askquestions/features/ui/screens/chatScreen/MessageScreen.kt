package com.fredy.askquestions.features.ui.screens.chatScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageEvent
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageState
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.UIState

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    state: UIState,
    messages: LazyPagingItems<Message>,
    onEvent: (MessageEvent) -> Unit,
) {

    val context = LocalContext.current
    LaunchedEffect(key1 = messages.loadState) {
        if (messages.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (messages.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (messages.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(
                        1f
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            MessageBody(
                modifier = Modifier.weight(
                    1f
                ),
                messages,
            )
        }

        // Input field and send button with animation
        val visibleState = state.inputText.isNotBlank()

        MessageTextField(
            inputText = state.inputText,
            onEvent = onEvent,
            visibleState = visibleState
        )
    }
}


