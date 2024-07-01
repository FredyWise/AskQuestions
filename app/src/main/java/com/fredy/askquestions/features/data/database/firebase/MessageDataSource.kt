package com.fredy.askquestions.features.data.database.firebase


import com.fredy.askquestions.features.data.apis.ApiConfiguration
import com.fredy.askquestions.features.data.database.firebase.models.MessageCollection
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

interface MessageDataSource {
    suspend fun upsertMessage(messageCollection: MessageCollection)
    suspend fun deleteMessage(messageCollection: MessageCollection)
    fun getMessagesFromChat(chatId: String): Flow<List<MessageCollection>>
    fun getPagerMessagesFromChat(
        chatId: String,
        lastMessageTime: Timestamp? = null,
        limit: Long = 20,
    ): Flow<List<MessageCollection>>

    fun searchMessages(
        text: String, userId: String
    ): Flow<List<MessageCollection>>
}

class MessageDataSourceImpl(
    private val firestore: FirebaseFirestore,
): MessageDataSource {

    private val messageCollection = firestore.collection(
        ApiConfiguration.FirebaseModel.MESSAGE_ENTITY
    )

    override suspend fun upsertMessage(
        messageCollection: MessageCollection
    ) {
        withContext(Dispatchers.IO) {
            val realMessage = if (messageCollection.messageId.isEmpty()) {
                val newMessageRef = this@MessageDataSourceImpl.messageCollection.document()
                messageCollection.copy(
                    messageId = newMessageRef.id,
                )
            } else {
                messageCollection
            }
            this@MessageDataSourceImpl.messageCollection.document(
                realMessage.messageId
            ).set(
                realMessage
            )
        }
    }

    override suspend fun deleteMessage(
        messageCollection: MessageCollection
    ) {
        withContext(Dispatchers.IO) {
            this@MessageDataSourceImpl.messageCollection.document(
                messageCollection.messageId
            ).delete()
        }
    }

    override fun getMessagesFromChat(
        chatId: String
    ): Flow<List<MessageCollection>> {
        Timber.d("getMessagesFromChat.start: $chatId")
        return try {
            val querySnapshot = messageCollection.whereEqualTo(
                "chatId", chatId
            ).orderBy(
                "timestamp",
                Query.Direction.ASCENDING
            ).snapshots()

            querySnapshot.map {
                Timber.d("getMessagesFromChat.result: ${it.toObjects<MessageCollection>()}")
                it.toObjects<MessageCollection>()
            }
        } catch (e: Exception) {
            Timber.e(
                "Failed to get all message: ${e.message}"
            )
            throw e
        }

    }

    override fun getPagerMessagesFromChat(
        chatId: String,
        lastMessageTime: Timestamp?,
        limit: Long,
    ): Flow<List<MessageCollection>> {
        return try {
            Timber.d("getPagerMessagesFromChat.start: $chatId")
            val querySnapshot = messageCollection.whereEqualTo(
                "chatId", chatId
            ).orderBy(
                "timestamp",
                Query.Direction.DESCENDING
            ).limit(limit)

            val lastMessageQuery = if (lastMessageTime == null) {
                querySnapshot.snapshots()
            } else {
                querySnapshot.startAfter(
                    lastMessageTime
                ).snapshots()
            }

            lastMessageQuery.map {
                Timber.d("getPagerMessagesFromChat.result: ${it.toObjects<MessageCollection>()}")
                it.toObjects<MessageCollection>()
            }
        } catch (e: Exception) {
            Timber.e(
                "Failed to get all message: ${e.message}"
            )
            throw e
        }

    }


    override fun searchMessages(
        text: String, userId: String
    ): Flow<List<MessageCollection>> {
        return try {
            val querySnapshot = messageCollection.whereEqualTo(
                "senderId", userId
            ).where(
                Filter.arrayContains(
                    "text", text
                )
            ).orderBy(
                "timestamp",
                Query.Direction.ASCENDING
            ).snapshots()

            querySnapshot.map { it.toObjects<MessageCollection>() }
        } catch (e: Exception) {
            Timber.e(
                "Failed to get all message: ${e.message}"
            )
            throw e
        }

    }

}
