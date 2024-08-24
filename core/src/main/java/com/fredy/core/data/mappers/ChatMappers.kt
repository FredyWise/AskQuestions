package com.fredy.askquestions.features.data.mappers

import com.fredy.askquestions.features.data.database.firebase.dto.ChatCollection
import com.fredy.askquestions.features.data.database.room.dto.ChatEntity
import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.models.User

suspend fun ChatCollection.toChat(users: suspend (List<String>) -> List<User>): Chat {
    return Chat(
        chatId = chatId,
        chatName = chatName,
        imageUrl = imageUrl,
        lastMessageText = lastMessageText,
        lastMessageTime = lastMessageTime,
        lastMessageSender = lastMessageSender,
        participants = users(participants)
    )
}

fun ChatCollection.toChatEntity(): ChatEntity {
    return ChatEntity(
        chatId = chatId,
        chatName = chatName,
        imageUrl = imageUrl,
        lastMessageText = lastMessageText,
        lastMessageTime = lastMessageTime,
        lastMessageSender = lastMessageSender,
        participants = participants
    )
}

suspend fun ChatEntity.toChat(users: suspend (List<String>) -> List<User>): Chat {
    return Chat(
        chatId = chatId,
        chatName = chatName,
        imageUrl = imageUrl,
        lastMessageText = lastMessageText,
        lastMessageTime = lastMessageTime,
        lastMessageSender = lastMessageSender,
        participants = users(participants)
    )
}

fun Chat.toChatCollection(): ChatCollection {
    return ChatCollection(
        chatId = chatId,
        chatName = chatName,
        imageUrl = imageUrl,
        lastMessageText = lastMessageText,
        lastMessageTime = lastMessageTime,
        lastMessageSender = lastMessageSender,
        participants = participants.map { it.uid }
    )
}
fun Chat.toChatEntity(): ChatEntity {
    return ChatEntity(
        chatId = chatId,
        chatName = chatName,
        imageUrl = imageUrl,
        lastMessageText = lastMessageText,
        lastMessageTime = lastMessageTime,
        lastMessageSender = lastMessageSender,
        participants = participants.map { it.uid }
    )
}