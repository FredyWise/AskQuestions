package com.fredy.askquestions.features.data.database.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 0,
    val chatId: String = "", // Unique identifier for the chat
    val chatName: String? = null, // Chat name (optional for group chats)
    val imageUrl: String? = null, // URL of the chat image (optional)
    val lastMessageText: String? = null, // Last message text
    val lastMessageTime: Timestamp? = null, // Time of the last message
    val lastMessageSender: String? = null, // Sender of the last message
    val participants: List<String> = emptyList() // List of participants id in the chat
)