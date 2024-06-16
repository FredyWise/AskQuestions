package com.fredy.askquestions.features.data.repositoryImpl

import com.fredy.askquestions.features.data.database.firebase.ChatDataSource
import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val chatDataSource: ChatDataSource,
): ChatRepository {
    override suspend fun upsertChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            chatDataSource.upsertChat(chat)
        }
    }

    override suspend fun deleteChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            chatDataSource.deleteChat(chat)
        }
    }

    override fun getChat(chatId: String): Flow<Chat?> {
        return flow {
            val chat = withContext(Dispatchers.IO) {
                chatDataSource.getChat(chatId)
            }
            emit(chat)
        }
    }

    override suspend fun getAllChatsOrderedByName(): Flow<List<Chat>> {
        val currentUser = firebaseAuth.currentUser!!
        return chatDataSource.getAllChatsOrderedByName(currentUser.uid)
    }

    override suspend fun searchChats(chatName: String): Flow<List<Chat>> {
        val currentUser = firebaseAuth.currentUser!!
        return chatDataSource.searchChats(
            chatName, currentUser.uid
        )
    }

}