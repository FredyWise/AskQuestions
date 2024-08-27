package com.fredy.core.data.repositoryImpl

import com.fredy.core.data.database.firebase.dao.UserDataSource
import com.fredy.core.data.mappers.toUser
import com.fredy.core.data.mappers.toUserCollection
import com.fredy.core.domain.models.User

import com.fredy.core.domain.repositories.UserRepository
import com.fredy.core.util.resource.DataError
import com.fredy.core.util.resource.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userDataSource: UserDataSource,
): UserRepository {
    override suspend fun upsertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDataSource.upsertUser(user.toUserCollection())
        }
    }

    override suspend fun deleteUser(user: User) {
        withContext(Dispatchers.IO) {
            userDataSource.deleteUser(user.toUserCollection())
        }
    }

    override fun getUser(userId: String): Flow<User?> {
        return flow {
            val user = withContext(Dispatchers.IO) {
                userDataSource.getUser(userId)
            }
            emit(user?.toUser())
        }
    }

    override suspend fun getCurrentUserFlow(): Flow<Resource<User?, DataError.Database>> = flow<Resource<User?, DataError.Database>> {
        emit(Resource.Loading())
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val user = withContext(Dispatchers.IO) {
                userDataSource.getUser(currentUser.uid)
            }
            emit(Resource.Success(user?.toUser()))
        }
    }.catch { e ->
        Timber.i(
            "getCurrentUser.Error: $e"
        )
        emit(Resource.Error(DataError.Database.UNKNOWN))
    }

    override suspend fun getCurrentUser() = userDataSource.getUser(
        firebaseAuth.currentUser?.uid ?: "-1"
    )?.toUser()

    override suspend fun getAllUsersOrderedByName(): Flow<List<User>> {
        return userDataSource.getAllUsersOrderedByName().map { users -> users.map { it.toUser() } }
    }

    override suspend fun searchUsers(usernameEmail: String): Flow<List<User>> {
        return userDataSource.searchUsers(
            usernameEmail
        ).map { users -> users.map { it.toUser() } }
    }

}