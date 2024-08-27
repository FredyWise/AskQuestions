package com.fredy.chat.data

import com.fredy.chat.data.mappers.toChat
import com.fredy.chat.data.mappers.toChatCollection
import com.fredy.chat.data.mappers.toMessage
import com.fredy.chat.data.mappers.toMessageCollection
import com.fredy.chat.data.mappers.toMessageEntity
import com.fredy.chat.domain.ChatRepository
import com.fredy.core.data.database.firebase.dao.ChatDataSource
import com.fredy.core.data.database.firebase.dao.MessageDataSource
import com.fredy.core.data.database.firebase.dao.UserDataSource
import com.fredy.core.data.database.room.dao.MessageDao
import com.fredy.core.data.mappers.toUser
import com.fredy.core.domain.models.Chat
import com.fredy.core.domain.models.Message
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import dagger.internal.Provider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firebaseUserProvider: Provider<FirebaseUser>,
    private val chatDataSource: ChatDataSource,
    private val messageDataSource: MessageDataSource,
    private val userDataSource: UserDataSource,
    private val messageDao: MessageDao,
): ChatRepository {
    override suspend fun upsertChat(chat: Chat): String {
        return withContext(Dispatchers.IO) {
            val currentUser = firebaseUserProvider.get()!!
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
        messageCollection: Message
    ): String {
        return withContext(Dispatchers.IO) {
            val currentUser = firebaseUserProvider.get()!!
            val tempMessage = if (messageCollection.senderId.isEmpty()) messageCollection.copy(
                senderId = currentUser.uid
            ) else messageCollection
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
        messageCollection: Message
    ) {
        withContext(Dispatchers.IO) {
            messageDataSource.deleteMessage(
                messageCollection.toMessageCollection()
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
        val currentUser = firebaseUserProvider.get()!!
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
        val currentUser = firebaseUserProvider.get()!!
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
        val currentUser = firebaseUserProvider.get()!!
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
        val currentUser = firebaseUserProvider.get()!!
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