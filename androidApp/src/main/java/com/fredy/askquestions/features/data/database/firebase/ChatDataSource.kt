package com.fredy.askquestions.features.data.database.firebase


import com.fredy.askquestions.features.data.apis.ApiConfiguration
import com.fredy.askquestions.features.domain.models.Chat
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

interface ChatDataSource {
    suspend fun upsertChat(chat: Chat)
    suspend fun deleteChat(chat: Chat)
    suspend fun getChat(chatId: String): Chat?
    suspend fun getAllChatsOrderedByName(userId:String): Flow<List<Chat>>
    suspend fun searchChats(chatName: String,userId:String): Flow<List<Chat>>
}

class ChatDataSourceImpl(
    private val firestore: FirebaseFirestore,
) : ChatDataSource {

    private val chatCollection = firestore.collection(ApiConfiguration.FirebaseModel.CHAT_ENTITY)

    override suspend fun upsertChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            val realChat = if (chat.chatId.isEmpty()) {
                val newChatRef = chatCollection.document()
                chat.copy(
                    chatId = newChatRef.id,
                )
            } else {
                chat
            }
            chatCollection.document(chat.chatId).set(realChat)
        }
    }

    override suspend fun deleteChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            chatCollection.document(chat.chatId).delete()
        }
    }

    override suspend fun getChat(chatId: String): Chat? {
        return withContext(Dispatchers.IO) {
            try {
                chatCollection.document(chatId).get().await().toObject<Chat>()
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get chat: ${e.message}"
                )
                throw e
            }
        }

    }

    override suspend fun getAllChatsOrderedByName(userId:String): Flow<List<Chat>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = chatCollection.orderBy(
                    "chatName", Query.Direction.ASCENDING
                ).snapshots()

                querySnapshot.map { it.toObjects<Chat>() }
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get all chat: ${e.message}"
                )
                throw e
            }
        }

    }

    override suspend fun searchChats(chatName: String, userId:String): Flow<List<Chat>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = chatCollection.where(
                    Filter.arrayContains(
                        "chatName", chatName
                    )
                ).orderBy(
                    "chatName", Query.Direction.ASCENDING
                ).snapshots()

                querySnapshot.map { it.toObjects<Chat>() }
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get all chat: ${e.message}"
                )
                throw e
            }

        }
    }
}
