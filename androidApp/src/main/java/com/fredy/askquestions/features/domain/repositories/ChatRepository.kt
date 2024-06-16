package com.fredy.askquestions.features.domain.repositories

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.util.Resource.DataError
import com.fredy.askquestions.features.domain.util.Resource.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun upsertChat(user: Chat)
    suspend fun deleteChat(user: Chat)
    fun getChat(userId: String): Flow<Chat?>
    suspend fun getAllChatsOrderedByName(): Flow<List<Chat>>
    suspend fun searchChats(usernameEmail: String): Flow<List<Chat>>
}

