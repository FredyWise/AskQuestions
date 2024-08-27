package com.fredy.chat.domain

import com.fredy.core.domain.models.Chat
import com.fredy.core.domain.models.Message
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun upsertChat(chat: Chat): String
    suspend fun deleteChat(chat: Chat)
    fun getChat(chatId: String): Flow<Chat?>
    fun getAllChatsOrderedByName(): Flow<List<Chat>>
    fun searchChats(chatName: String): Flow<List<Chat>>

    suspend fun upsertMessage(messageCollection: Message):String
    suspend fun deleteMessage(messageCollection: Message)
    fun searchMessages(messageName: String): Flow<List<Message>>
    fun getAllMessagesInTheSameChat(
        chatId: String,
        lastMessageTime: Timestamp? = null,
        limit: Int = 20
    ): Flow<List<Message>>

}

