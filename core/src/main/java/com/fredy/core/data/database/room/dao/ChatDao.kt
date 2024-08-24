package com.fredy.askquestions.features.data.database.room.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.fredy.askquestions.features.data.database.room.dto.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Upsert
    suspend fun upsertChat(chat: ChatEntity)
    @Upsert
    suspend fun upsertAllChats(chats: List<ChatEntity>)
    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    @Query("DELETE FROM ChatEntity")
    suspend fun clearAllChats()

    @Query("SELECT * FROM ChatEntity WHERE chatId = :chatId")
    suspend fun getChat(chatId: String): ChatEntity?

    @Query("SELECT * FROM ChatEntity WHERE participants LIKE '%' || :userId || '%' ORDER BY chatName ASC")
    fun getAllChatsOrderedByName(userId: String): Flow<List<ChatEntity>>

    @Query("SELECT * FROM ChatEntity WHERE chatName LIKE '%' || :chatName || '%' AND participants LIKE '%' || :userId || '%' ORDER BY chatName ASC")
    fun searchChats(chatName: String, userId: String): Flow<List<ChatEntity>>
}