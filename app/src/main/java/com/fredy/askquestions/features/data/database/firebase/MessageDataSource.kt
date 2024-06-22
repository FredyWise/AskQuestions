package com.fredy.askquestions.features.data.database.firebase


import com.fredy.askquestions.features.data.apis.ApiConfiguration
import com.fredy.askquestions.features.domain.models.Message
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
    suspend fun upsertMessage(message: Message)
    suspend fun deleteMessage(message: Message)
    fun getMessagesFromChat(chatId: String): Flow<List<Message>>
    fun searchMessages(
        text: String, userId: String
    ): Flow<List<Message>>
}

class MessageDataSourceImpl(
    private val firestore: FirebaseFirestore,
): MessageDataSource {

    private val messageCollection = firestore.collection(
        ApiConfiguration.FirebaseModel.MESSAGE_ENTITY
    )

    override suspend fun upsertMessage(message: Message) {
        withContext(Dispatchers.IO) {
            val realMessage = if (message.messageId.isEmpty()) {
                val newMessageRef = messageCollection.document()
                message.copy(
                    messageId = newMessageRef.id,
                )
            } else {
                message
            }
            messageCollection.document(realMessage.messageId).set(
                realMessage
            )
        }
    }

    override suspend fun deleteMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageCollection.document(message.messageId).delete()
        }
    }

    override fun getMessagesFromChat(
        chatId: String
    ): Flow<List<Message>> {
        return try {
            val querySnapshot = messageCollection.whereEqualTo(
                "chatId",
                chatId
            ).orderBy(
                "timestamp",
                Query.Direction.ASCENDING
            ).snapshots()

            querySnapshot.map {
                it.toObjects<Message>()
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
    ): Flow<List<Message>> {
        return try {
            val querySnapshot = messageCollection.where(
                Filter.arrayContains(
                    "text", text
                )
            ).orderBy(
                "timestamp",
                Query.Direction.ASCENDING
            ).snapshots()

            querySnapshot.map { it.toObjects<Message>() }
        } catch (e: Exception) {
            Timber.e(
                "Failed to get all message: ${e.message}"
            )
            throw e
        }

    }

}
