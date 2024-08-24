package com.fredy.askquestions.features.ui.screens.chatScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.fredy.askquestions.features.data.database.converters.TimestampConverter
import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.theme.components.AnimatedFloatingActionButton
import com.fredy.theme.components.PullToRefreshLazyColumn
import com.fredy.theme.components.SingleLineText
import com.fredy.askquestions.features.ui.util.formatTime
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
//    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.primary,
//    containerColor: Color = MaterialTheme.colorScheme.surface,
    viewModel: ChatViewModel,
    navigateToMessageScreen: (String?) -> Unit,
) {
    val chatList by viewModel.chatList.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            AnimatedFloatingActionButton(
                shape = MaterialTheme.shapes.medium,
                icon = {
                    Icon(
                        Icons.Default.ChatBubble,
                        contentDescription = "Add Chat",
                        tint = contentColor,
                    )
                },
                onShowFab = { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == lazyListState.layoutInfo.totalItemsCount - 1 },
                onClick = {
                    navigateToMessageScreen(
                        null
                    )
                },
            )
        },
    ) { _ ->
        Column(
            modifier = Modifier
        ) {
            if (chatList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No chats found",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                PullToRefreshLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    lazyListState = lazyListState,
                    items = chatList,
                    content = { chat ->
                        ChatListItem(
                            chat = chat,
                            onClick = {
                                navigateToMessageScreen(
                                    chat.chatId
                                )
                            },
                        )
                    },
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        scope.launch {
                            isRefreshing = true
                            delay(3000L) // Simulated API call
                            isRefreshing = false
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun ChatListItem(
    chat: Chat, onClick: () -> Unit
) {
    Card(onClick = onClick) {
        Row(modifier = Modifier.padding(14.dp)) {
            // Avatar (unchanged)
            if (chat.imageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(
                        chat.imageUrl
                    ),
                    contentDescription = "Chat avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(
                            CircleShape
                        )
                )
            } else if (chat.chatName != null) {
                Box(
                    modifier = Modifier
                        .clip(
                            CircleShape
                        )
                        .background(
                            Color(0xFF957fef)
                        )
                        .size(40.dp),
                    contentAlignment = Alignment.
                    Center
                ) {
                    Text(
                        text = chat.chatName.first().toString(),
                        color = Color.White
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(
                            CircleShape
                        )
                        .background(
                            Color.Gray
                        )
                        .padding(3.dp),
                    contentDescription = "Chat avatar placeholder",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                SingleLineText(
                    text = chat.chatName ?: chat.participants.joinToString { it.username.toString() },
                    style = MaterialTheme.typography.titleMedium,
                )
                if (chat.lastMessageText != null && chat.lastMessageTime != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SingleLineText(
                            text = chat.lastMessageText,
                            modifier = Modifier.weight(1f),
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        SingleLineText(
                            text = TimestampConverter.toDateTime(
                                chat.lastMessageTime
                            ).formatTime(),
                            color = Color.Gray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}




