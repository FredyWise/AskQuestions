package com.fredy.chat.data.mappers

import com.fredy.core.data.database.firebase.dto.MessageCollection
import com.fredy.core.data.database.room.dto.MessageEntity
import com.fredy.core.domain.models.Message

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