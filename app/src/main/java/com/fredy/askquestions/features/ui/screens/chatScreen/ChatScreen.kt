package com.fredy.askquestions.features.ui.screens.chatScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.fredy.askquestions.features.ui.util.formatTime
import com.fredy.askquestions.features.ui.util.truncateString
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.ChatViewModel

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel,
    navigateToMessageScreen: (String?) -> Unit,
) {
    val chatList by viewModel.chatList.collectAsStateWithLifecycle()
    Surface(
        modifier = modifier,
    ) {
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(chatList) { chat ->
                        Spacer(
                            modifier = Modifier.height(
                                8.dp
                            )
                        )
                        ChatListItem(
                            chat = chat,
                            onClick = {
                                navigateToMessageScreen(
                                    chat.chatId
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatListItem(
    chat: Chat, onClick: () -> Unit
) {
    Card(onClick = onClick) {
        Row(modifier = Modifier.padding(16.dp)) {
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
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Chat avatar placeholder"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Chat details
            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = chat.chatName ?: chat.participants.joinToString { it.username!! },
//                    style = MaterialTheme.typography.titleLarge
//                )
                // Last Message and Timestamp
                if (chat.lastMessageText != null && chat.lastMessageTime != null) {
                    Text(
                        text = chat.lastMessageText.truncateString(),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // Timestamp
                    Text(
                        text = TimestampConverter.toDateTime(
                            chat.lastMessageTime
                        ).formatTime(),
                        color = Color.Gray,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}




