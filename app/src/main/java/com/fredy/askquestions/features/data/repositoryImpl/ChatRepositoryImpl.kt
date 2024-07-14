package com.fredy.askquestions.features.data.repositoryImpl

import com.fredy.askquestions.features.data.database.firebase.dao.ChatDataSource
import com.fredy.askquestions.features.data.database.firebase.dao.MessageDataSource
import com.fredy.askquestions.features.data.database.firebase.models.MessageCollection
import com.fredy.askquestions.features.data.database.room.dao.MessageDao
import com.fredy.askquestions.features.data.mappers.toMessageEntity
import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val chatDataSource: ChatDataSource,
    private val messageDataSource: MessageDataSource,
    private val messageDao: MessageDao
): ChatRepository {
    override suspend fun upsertChat(chat: Chat): String {
        return withContext(Dispatchers.IO) {
            val currentUser = firebaseAuth.currentUser!!
            var tempChat = chat.copy(
                lastMessageSender = currentUser.uid
            )
            Timber.d("ChatRepositoryImpl.upsertChat: $tempChat")
            if (!tempChat.participants.contains(
                    currentUser.uid
                )) {
                tempChat = tempChat.copy(
                    participants = tempChat.participants + listOf(
                        currentUser.uid
                    ),
                )
            }
            chatDataSource.upsertChat(tempChat)
        }
    }

    override suspend fun upsertMessage(
        messageCollection: MessageCollection
    ): String {
       return withContext(Dispatchers.IO) {
            val currentUser = firebaseAuth.currentUser!!
            val tempMessage = if (messageCollection.senderId.isEmpty()) messageCollection.copy(
                senderId = currentUser.uid
            ) else messageCollection
            Timber.d("ChatRepositoryImpl.upsertMessage: $tempMessage")
            val messageId = messageDataSource.upsertMessage(
                tempMessage
            )
            messageDao.upsertMessage(tempMessage.copy(messageId = messageId).toMessageEntity())
            messageId
        }
    }

    override suspend fun deleteChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            chatDataSource.deleteChat(chat)
        }
    }

    override suspend fun deleteMessage(
        messageCollection: MessageCollection
    ) {
        withContext(Dispatchers.IO) {
            messageDataSource.deleteMessage(
                messageCollection
            )
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
        return chatDataSource.getAllChatsOrderedByName(
            currentUser.uid
        )
    }

    override fun getAllMessagesInTheSameChat(
        chatId: String,
        lastMessageTime: Timestamp?,
        limit: Int,
    ): Flow<List<MessageCollection>> {
        Timber.d("ChatRepositoryImpl.getAllMessagesInTheSameChat.start: $chatId")
        return messageDataSource.getPagerMessagesFromChat(
            chatId = chatId,
            lastMessageTime = lastMessageTime,
            limit = limit
        )
    }

    override fun searchChats(chatName: String): Flow<List<Chat>> {
        val currentUser = firebaseAuth.currentUser!!
        return chatDataSource.searchChats(
            chatName, currentUser.uid
        )
    }

    override fun searchMessages(messageName: String): Flow<List<MessageCollection>> {
        val currentUser = firebaseAuth.currentUser!!
        return messageDataSource.searchMessages(
            messageName, currentUser.uid
        )
    }
}