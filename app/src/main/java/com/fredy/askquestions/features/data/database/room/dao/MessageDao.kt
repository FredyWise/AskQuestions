package com.fredy.askquestions.features.data.database.room.dao


import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.fredy.askquestions.features.data.database.room.models.MessageEntity
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMessage(messageEntity: MessageEntity)

    @Upsert
    suspend fun upsertAllMessages(messages: List<MessageEntity>)

    @Delete
    suspend fun deleteMessage(messageEntity: MessageEntity)

    @Query("DELETE FROM MessageEntity")
    suspend fun clearAllMessages()

    @Query("SELECT * FROM MessageEntity WHERE chatId = :chatId ORDER BY timestamp DESC")
    fun getPagingSource(chatId: String): PagingSource<Int, MessageEntity>

    @Query("SELECT * FROM MessageEntity WHERE chatId = :chatId ORDER BY timestamp DESC")
    fun getMessagesFromChat(chatId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM MessageEntity WHERE text LIKE '%' || :text || '%' AND senderId = :userId ORDER BY timestamp ASC")
    fun searchMessages(
        text: String, userId: String
    ): Flow<List<MessageEntity>>
}