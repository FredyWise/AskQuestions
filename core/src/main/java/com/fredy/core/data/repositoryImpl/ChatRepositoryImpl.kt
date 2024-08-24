package com.fredy.askquestions.features.data.repositoryImpl

import com.fredy.askquestions.features.data.database.firebase.dao.ChatDataSource
import com.fredy.askquestions.features.data.database.firebase.dao.MessageDataSource
import com.fredy.askquestions.features.data.database.firebase.dao.UserDataSource
import com.fredy.askquestions.features.data.database.room.dao.MessageDao
import com.fredy.askquestions.features.data.mappers.toChat
import com.fredy.askquestions.features.data.mappers.toChatCollection
import com.fredy.askquestions.features.data.mappers.toMessage
import com.fredy.askquestions.features.data.mappers.toMessageCollection
import com.fredy.askquestions.features.data.mappers.toMessageEntity
import com.fredy.askquestions.features.data.mappers.toUser
import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val chatDataSource: ChatDataSource,
    private val messageDataSource: MessageDataSource,
    private val userDataSource: UserDataSource,
    private val messageDao: MessageDao,
): ChatRepository {
    override suspend fun upsertChat(chat: Chat): String {
        return withContext(Dispatchers.IO) {
            val currentUser = firebaseAuth.currentUser!!
            var tempChat = chat.copy(
                lastMessageSender = currentUser.uid
            ).toChatCollection()
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
        message: Message
    ): String {
        return withContext(Dispatchers.IO) {
            val currentUser = firebaseAuth.currentUser!!
            val tempMessage = if (message.senderId.isEmpty()) message.copy(
                senderId = currentUser.uid
            ) else message
            Timber.d("ChatRepositoryImpl.upsertMessage: $tempMessage")
            val messageId = messageDataSource.upsertMessage(
                tempMessage.toMessageCollection()
            )
            messageDao.upsertMessage(
                tempMessage.copy(
                    messageId = messageId
                ).toMessageEntity()
            )
            messageId
        }
    }

    override suspend fun deleteChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            chatDataSource.deleteChat(chat.toChatCollection())
        }
    }

    override suspend fun deleteMessage(
        message: Message
    ) {
        withContext(Dispatchers.IO) {
            messageDataSource.deleteMessage(
                message.toMessageCollection()
            )
        }
    }

    override fun getChat(chatId: String): Flow<Chat?> {
        return flow {
            val chat = withContext(Dispatchers.IO) {
                chatDataSource.getChat(chatId)
            }
            emit(chat?.toChat { participants ->
                userDataSource.getParticipants(
                    participants
                ).first().map { it.toUser() }
            })
        }
    }

    override fun getAllChatsOrderedByName(): Flow<List<Chat>> {
        val currentUser = firebaseAuth.currentUser!!
        return chatDataSource.getAllChatsOrderedByName(
            currentUser.uid
        ).map { chatCollections ->
            chatCollections.map {
                it.toChat { participants ->
                    userDataSource.getParticipants(
                        participants
                    ).first().map { it.toUser() }
                }
            }
        }
    }

    override fun getAllMessagesInTheSameChat(
        chatId: String,
        lastMessageTime: Timestamp?,
        limit: Int,
    ): Flow<List<Message>> {
        Timber.d("ChatRepositoryImpl.getAllMessagesInTheSameChat.start: $chatId")
        val currentUser = firebaseAuth.currentUser!!
        return messageDataSource.getPagerMessagesFromChat(
            chatId = chatId,
            lastMessageTime = lastMessageTime,
            limit = limit
        ).map { messageCollections ->
            messageCollections.map {
                it.toMessage(
                    currentUser.uid
                )
            }
        }
    }

    override fun searchChats(chatName: String): Flow<List<Chat>> {
        val currentUser = firebaseAuth.currentUser!!
        return chatDataSource.searchChats(
            chatName, currentUser.uid
        ).map { chatCollections ->
            chatCollections.map {
                it.toChat { participants ->
                    userDataSource.getParticipants(
                        participants
                    ).first().map { it.toUser() }
                }
            }
        }
    }

    override fun searchMessages(messageName: String): Flow<List<Message>> {
        val currentUser = firebaseAuth.currentUser!!
        return messageDataSource.searchMessages(
            messageName, currentUser.uid
        ).map { messageCollections ->
            messageCollections.map {
                it.toMessage(
                    currentUser.uid
                )
            }
        }
    }
}