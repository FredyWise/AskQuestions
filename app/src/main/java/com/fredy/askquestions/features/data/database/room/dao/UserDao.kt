package com.fredy.askquestions.features.data.database.room.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fredy.askquestions.features.data.database.room.dto.MessageEntity
import com.fredy.askquestions.features.data.database.room.dto.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao

interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE uid = :userId")
    suspend fun getUser(userId: String): UserEntity?


    @Query("SELECT * FROM UserEntity WHERE uid = :uid ORDER BY username ASC")
    fun getParticipants(uid: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM UserEntity ORDER BY username ASC")
    fun getAllUsersOrderedByName(): Flow<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE username LIKE '%' || :usernameEmail || '%' OR email LIKE '%' || :usernameEmail || '%' ORDER BY username ASC")
    fun searchUsers(usernameEmail: String): Flow<List<UserEntity>>
}