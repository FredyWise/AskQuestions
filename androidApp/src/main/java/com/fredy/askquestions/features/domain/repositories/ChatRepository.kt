package com.fredy.askquestions.features.domain.repositories

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.util.Resource.DataError
import com.fredy.askquestions.features.domain.util.Resource.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun upsertChat(chat: Chat)
    suspend fun deleteChat(chat: Chat)
    fun getChat(chatId: String): Flow<Chat?>
    suspend fun getAllChatsOrderedByName(): Flow<List<Chat>>
    suspend fun searchChats(chatName: String): Flow<List<Chat>>

    suspend fun upsertMessage(message: Message)
    suspend fun deleteMessage(message: Message)
    suspend fun searchMessages(messageName: String): Flow<List<Message>>
}

