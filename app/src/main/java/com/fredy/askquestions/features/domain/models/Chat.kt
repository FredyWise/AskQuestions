package com.fredy.askquestions.features.domain.models

import com.fredy.askquestions.features.data.database.firebase.dto.MessageCollection
import com.google.firebase.Timestamp

data class Chat(
    val chatId: String = "", // Unique identifier for the chat
    val chatName: String? = null, // Chat name (optional for group chats)
    val imageUrl: String? = null, // URL of the chat image (optional)
    val lastMessageText: String? = null, // Last message text
    val lastMessageTime: Timestamp? = null, // Time of the last message
    val lastMessageSender: String? = null, // Sender of the last message
    val participants: List<String> = emptyList() // List of participants id in the chat
) {
    fun updateLastMessage(messageCollection: MessageCollection): Chat {
        return this.copy(
            lastMessageText = messageCollection.text,
            lastMessageTime = messageCollection.timestamp,
        )
    }

}