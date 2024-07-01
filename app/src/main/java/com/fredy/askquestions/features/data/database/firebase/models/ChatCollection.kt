package com.fredy.askquestions.features.data.database.firebase.models

import com.google.firebase.Timestamp

data class ChatCollection(
    val chatId: String = "", // Unique identifier for the chat
    val chatName: String? = null, // Chat name (optional for group chats)
    val imageUrl: String? = null, // URL of the chat image (optional)
    val lastMessageText: String? = null, // Last message text
    val lastMessageTime: Timestamp? = null, // Time of the last message
    val lastMessageSender: String? = null, // Sender of the last message
    val participants: List<String> = emptyList() // List of participants id in the chat
)