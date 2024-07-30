package com.fredy.askquestions.features.data.database.firebase.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
@Entity
data class MessageCollection(
    @PrimaryKey
    val messageId: String = "",
    val senderId: String = "",
    val chatId: String = "",
    val text: String = "",
    val mediaUrls: List<String> = emptyList(),
    val timestamp: Timestamp = Timestamp.now(),
)

