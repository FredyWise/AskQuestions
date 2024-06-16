package com.fredy.askquestions.features.domain.models

import com.google.firebase.Timestamp

data class Message(
    val messageId: String,
    val senderId: String,
    val receiverId: String,
    val chatId: String,
    val message: String,
    val mediaUrls: List<String> = emptyList(),
    val timestamp: Timestamp,
){

}

