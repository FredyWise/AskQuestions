package com.fredy.askquestions.features.ui.screens.chatScreen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.ui.screens.comonComponents.Templates.AnimatedFloatingActionButton
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageEvent
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageState
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.UIState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    state: UIState,
    messages: LazyPagingItems<Message>,
    onEvent: (MessageEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

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

    Scaffold(
        floatingActionButton = {
            Column(Modifier.padding(bottom = 75.dp, end = 5.dp)) {
                AnimatedFloatingActionButton(
                    modifier = Modifier.size(30.dp),
                    shape = MaterialTheme.shapes.medium,
                    icon = {
                        Icon(
                            Icons.Default.ArrowDownward,
                            contentDescription = "go to newest message",
                            tint = contentColor,
                        )
                    },
                    onShowFab = { lazyListState.firstVisibleItemScrollOffset < 3 },
                    onClick = {
                        scope.launch {
                            lazyListState.animateScrollToItem(
                                0
                            )
                        }
                    },
                )
            }
        },
    ) { _ ->
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (messages.loadState.refresh is LoadState.Loading) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(
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
                    messages = messages,
                    lazyListState = lazyListState,
                )
            }

            val visibleState = state.inputText.isNotBlank()

            MessageTextField(
                inputText = state.inputText,
                onEvent = onEvent,
                visibleState = visibleState
            )
        }
    }
}


