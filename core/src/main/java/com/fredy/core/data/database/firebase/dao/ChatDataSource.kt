package com.fredy.askquestions.features.data.database.firebase.dao


import com.fredy.core.data.Util.Configuration
import com.fredy.askquestions.features.data.database.firebase.dto.ChatCollection
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
    suspend fun upsertChat(chat: ChatCollection): String
    suspend fun deleteChat(chat: ChatCollection)
    suspend fun getChat(chatId: String): ChatCollection?
    fun getAllChatsOrderedByName(userId: String): Flow<List<ChatCollection>>
    fun searchChats(
        chatName: String, userId: String
    ): Flow<List<ChatCollection>>
}

class ChatDataSourceImpl(
    private val firestore: FirebaseFirestore,
): ChatDataSource {

    private val chatCollection = firestore.collection(
        Configuration.FirebaseModel.CHAT_ENTITY
    )

    override suspend fun upsertChat(chat: ChatCollection): String {
        return withContext(Dispatchers.IO) {
            val realChat = if (chat.chatId.isEmpty()) {
                val newChatRef = chatCollection.document()
                chat.copy(
                    chatId = newChatRef.id,
                )
            } else {
                chat
            }
            chatCollection.document(realChat.chatId).set(
                realChat
            ).addOnFailureListener {
                Timber.e(
                    "Failed to upsert chat: ${it.message}"
                )
            }
            realChat.chatId
        }

    }

    override suspend fun deleteChat(chat: ChatCollection) {
        withContext(Dispatchers.IO) {
            chatCollection.document(chat.chatId).delete()
        }
    }

    override suspend fun getChat(chatId: String): ChatCollection? {
        return withContext(Dispatchers.IO) {
            try {
                chatCollection.document(chatId).get().await().toObject<ChatCollection>()
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get chat: ${e.message}"
                )
                throw e
            }
        }

    }

    override fun getAllChatsOrderedByName(
        userId: String
    ): Flow<List<ChatCollection>> {
        return try {
            val querySnapshot = chatCollection.whereArrayContains(
                "participants", userId
            ).orderBy(
                "chatName",
                Query.Direction.ASCENDING
            ).snapshots()

            querySnapshot.map { it.toObjects<ChatCollection>() }
        } catch (e: Exception) {
            Timber.e(
                "Failed to get all chat: ${e.message}"
            )
            throw e
        }
    }

    override fun searchChats(
        chatName: String, userId: String
    ): Flow<List<ChatCollection>> {
        return try {
            val querySnapshot = chatCollection.whereArrayContains(
                "participants",
                userId
            ).where(
                Filter.arrayContains(
                    "chatName", chatName
                )
            ).orderBy(
                "chatName",
                Query.Direction.ASCENDING
            ).snapshots()

            querySnapshot.map { it.toObjects<ChatCollection>() }
        } catch (e: Exception) {
            Timber.e(
                "Failed to get all chat: ${e.message}"
            )
            throw e
        }

    }
}
