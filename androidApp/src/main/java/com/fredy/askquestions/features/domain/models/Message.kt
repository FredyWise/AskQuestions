package com.fredy.askquestions.features.domain.models

data class Message(
    val messageId: String,
    val senderId: String,
    val chatId: String,
    val message: String,
    val mediaUrls: List<String> = emptyList()
)

