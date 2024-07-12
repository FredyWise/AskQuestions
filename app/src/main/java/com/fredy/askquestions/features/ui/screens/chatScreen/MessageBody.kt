package com.fredy.askquestions.features.ui.screens.chatScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.fredy.askquestions.features.domain.models.Message

@Composable
fun MessageBody(
    modifier: Modifier = Modifier,
    messages: LazyPagingItems<Message>
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(messages, messages.itemCount) {
        if (messages.itemCount > 0) lazyListState.animateScrollToItem(
            0
        )
    }
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = lazyListState,
        reverseLayout = true
    ) {
        items(
            messages.itemCount,
            key = messages.itemKey { it.timestamp },
        ) {
            messages[it]?.let {message ->
                MessageBubble(
                    text = message.text,
                    isRight = message.isUser
                )
            }
        }

        item {
            if (messages.loadState.append is LoadState.Loading) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}