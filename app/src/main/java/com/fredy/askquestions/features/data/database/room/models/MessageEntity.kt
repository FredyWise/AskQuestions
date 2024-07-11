package com.fredy.askquestions.features.data.database.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 0,
    val messageId: String = "",
    val senderId: String = "",
    val chatId: String = "",
    val text: String = "",
    val mediaUrls: List<String> = emptyList(),
    val timestamp: Timestamp = Timestamp.now(),
)

