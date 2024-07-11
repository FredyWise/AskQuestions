package com.fredy.askquestions.features.domain.repositories

import com.fredy.askquestions.features.data.database.firebase.models.MessageCollection
import com.fredy.askquestions.features.domain.models.Chat
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun upsertChat(chat: Chat): String
    suspend fun deleteChat(chat: Chat)
    fun getChat(chatId: String): Flow<Chat?>
    fun getAllChatsOrderedByName(): Flow<List<Chat>>
    fun searchChats(chatName: String): Flow<List<Chat>>

    suspend fun upsertMessage(messageCollection: MessageCollection)
    suspend fun deleteMessage(messageCollection: MessageCollection)
    fun searchMessages(messageName: String): Flow<List<MessageCollection>>
    fun getAllMessagesInTheSameChat(
        chatId: String,
        lastMessageTime: Timestamp? = null,
        limit: Int = 20
    ): Flow<List<MessageCollection>>

}

