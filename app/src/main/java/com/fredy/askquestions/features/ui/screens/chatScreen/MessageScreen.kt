package com.fredy.askquestions.features.ui.screens.chatScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageEvent
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageState
import timber.log.Timber

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    state: MessageState,
    messages: LazyPagingItems<Message>,
    onEvent: (MessageEvent) -> Unit,
) {

    val lazyListState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Messages display area

        LaunchedEffect(messages) {
            if (messages.itemCount > 0) lazyListState.animateScrollToItem(
                messages.itemCount-1
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(
                    1f
                ),
            state = lazyListState,
            reverseLayout = true,
        ) {
            items(
                messages.itemCount,
                key = { it },
            ) {
                messages[it]?.let {
                    MessageBubble(
                        text = it.text,
                        isRight = it.isUser
                    )
                }
            }
        }

//        LaunchedEffect(state.messages) {
//            if (state.messages.isNotEmpty()) lazyListState.animateScrollToItem(
//                state.messages.lastIndex
//            )
//        }
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(
//                    1f
//                ),
//            state = lazyListState,
//        ) {
//            items(state.messages,
//                key = { it.messageId }) { messageMap ->
//                MessageBubble(
//                    text = messageMap.text,
//                    isRight = messageMap.isUser
//                )
//            }
//        }


        // Input field and send button with animation
        val visibleState = state.inputText.isNotBlank()

        MessageTextField(
            inputText = state.inputText,
            onEvent = onEvent,
            visibleState = visibleState
        )
    }
}

