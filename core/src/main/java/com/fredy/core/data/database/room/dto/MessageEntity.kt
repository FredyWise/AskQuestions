package com.fredy.core.data.database.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
@Entity
data class MessageEntity(
    @PrimaryKey
    val messageId: String = "",
    val senderId: String = "",
    val chatId: String = "",
    val text: String = "",
    val mediaUrls: List<String> = emptyList(),
    val timestamp: Timestamp = Timestamp.now(),
)

