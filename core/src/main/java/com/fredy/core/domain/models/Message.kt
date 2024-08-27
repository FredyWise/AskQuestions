package com.fredy.core.domain.models

import com.google.firebase.Timestamp

data class Message (val messageId: String = "",
    val senderId: String = "",
    val chatId: String = "",
    val text: String = "",
    val mediaUrls: List<String> = emptyList(),
    val timestamp: Timestamp = Timestamp.now(),
    val isUser:Boolean = true,
)