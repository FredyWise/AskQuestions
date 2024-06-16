package com.fredy.askquestions.features.domain.repositories

import com.fredy.askquestions.features.domain.models.User
import com.fredy.askquestions.features.domain.util.Resource.DataError
import com.fredy.askquestions.features.domain.util.Resource.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun upsertUser(user: User)
    suspend fun deleteUser(user: User)
    fun getUser(userId: String): Flow<User?>
    suspend fun getCurrentUserFlow(): Flow<Resource<User?, DataError.Database>>
    suspend fun getCurrentUser(): User?
    suspend fun getAllUsersOrderedByName(): Flow<List<User>>
    suspend fun searchUsers(usernameEmail: String): Flow<List<User>>
}

