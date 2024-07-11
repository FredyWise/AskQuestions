package com.fredy.askquestions.features.data.mappers

import com.fredy.askquestions.features.data.database.firebase.models.MessageCollection
import com.fredy.askquestions.features.data.database.room.models.MessageEntity
import com.fredy.askquestions.features.domain.models.Message

fun MessageCollection.toMessage(userId: String): Message {
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

fun MessageCollection.toMessageEntity(): MessageEntity {
    return MessageEntity(
        messageId = this.messageId,
        senderId = this.senderId,
        chatId = this.chatId,
        text = this.text,
        mediaUrls = this.mediaUrls,
        timestamp = this.timestamp,
    )
}

fun MessageEntity.toMessage(userId: String): Message{
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

fun Message.toMessageCollection(): MessageCollection{
    return MessageCollection(
        messageId = this.messageId,
        senderId = this.senderId,
        chatId = this.chatId,
        text = this.text,
        mediaUrls = this.mediaUrls,
        timestamp = this.timestamp,
    )

}