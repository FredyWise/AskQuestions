package com.fredy.askquestions.features.data.mappers

import com.fredy.askquestions.features.data.database.firebase.dto.MessageCollection
import com.fredy.askquestions.features.data.database.room.dto.MessageEntity
import com.fredy.askquestions.features.domain.models.Message

fun MessageCollection.toMessage(userId: String): Message {
    return Message(
        messageId = messageId,
        senderId = senderId,
        chatId = chatId,
        text = text,
        mediaUrls = mediaUrls,
        timestamp = timestamp,
        isUser = senderId == userId
    )
}

fun MessageCollection.toMessageEntity(): MessageEntity {
    return MessageEntity(
        messageId = messageId,
        senderId = senderId,
        chatId = chatId,
        text = text,
        mediaUrls = mediaUrls,
        timestamp = timestamp,
    )
}

fun MessageEntity.toMessage(userId: String): Message {
    return Message(
        messageId = messageId,
        senderId = senderId,
        chatId = chatId,
        text = text,
        mediaUrls = mediaUrls,
        timestamp = timestamp,
        isUser = senderId == userId
    )
}

fun Message.toMessageCollection(): MessageCollection {
    return MessageCollection(
        messageId = messageId,
        senderId = senderId,
        chatId = chatId,
        text = text,
        mediaUrls = mediaUrls,
        timestamp = timestamp,
    )
}

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        messageId = messageId,
        senderId = senderId,
        chatId = chatId,
        text = text,
        mediaUrls = mediaUrls,
        timestamp = timestamp,
    )
}