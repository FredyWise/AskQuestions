package com.fredy.askquestions.features.data.repositoryImpl

import com.fredy.askquestions.features.data.database.firebase.ChatDataSource
import com.fredy.askquestions.features.data.database.firebase.MessageDataSource
import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.models.Message
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
    private val messageDataSource: MessageDataSource
): ChatRepository {
    override suspend fun upsertChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            chatDataSource.upsertChat(chat)
        }
    }

    override suspend fun upsertMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageDataSource.upsertMessage(message)
        }
    }

    override suspend fun deleteChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            chatDataSource.deleteChat(chat)
        }
    }

    override suspend fun deleteMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageDataSource.deleteMessage(message)
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

    override fun getAllChatsOrderedByName(): Flow<List<Chat>> {
        val currentUser = firebaseAuth.currentUser!!
        return chatDataSource.getAllChatsOrderedByName(currentUser.uid)
    }

    override fun getAllMessagesInTheSameChat(chatId: String): Flow<List<Message>> {
        return messageDataSource.getMessagesFromChat(chatId)
    }

    override fun searchChats(chatName: String): Flow<List<Chat>> {
        val currentUser = firebaseAuth.currentUser!!
        return chatDataSource.searchChats(
            chatName, currentUser.uid
        )
    }

    override fun searchMessages(messageName: String): Flow<List<Message>> {
        val currentUser = firebaseAuth.currentUser!!
        return messageDataSource.searchMessages(
            messageName, currentUser.uid
        )
    }
}