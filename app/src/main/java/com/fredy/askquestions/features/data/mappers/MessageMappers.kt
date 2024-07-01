package com.fredy.askquestions.features.data.mappers

import com.fredy.askquestions.features.data.database.firebase.models.MessageCollection
import com.fredy.askquestions.features.domain.models.Message

fun MessageCollection.toMessageMap(userId: String): Message {
    return Message(
        messageId = this.messageId,
        senderId = this.senderId,
        chatId = this.chatId,
        text = this.text,
        mediaUrls = this.mediaUrls,
        timestamp = this.timestamp,
        isUser = this.senderId == userId
    )
}