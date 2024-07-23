package com.fredy.askquestions.features.ui.screens.chatScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.ui.screens.comonComponents.Templates.AnimatedFloatingActionButton
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MessageBody(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    messages: LazyPagingItems<Message>
) {
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
            key = messages.itemKey { it.messageId },
        ) {
            messages[it]?.let { message ->
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