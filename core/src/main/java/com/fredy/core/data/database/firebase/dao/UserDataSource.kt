package com.fredy.core.data.database.firebase.dao


import com.fredy.core.data.util.Configuration
import com.fredy.core.data.database.firebase.dto.UserCollection
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

interface UserDataSource {
    suspend fun upsertUser(user: UserCollection)
    suspend fun deleteUser(user: UserCollection)
    suspend fun getUser(userId: String): UserCollection?
    suspend fun getParticipants(userIdList: List<String>): Flow<List<UserCollection>>
    suspend fun getAllUsersOrderedByName(): Flow<List<UserCollection>>
    suspend fun searchUsers(usernameEmail: String): Flow<List<UserCollection>>
}

class UserDataSourceImpl(
    private val firestore: FirebaseFirestore,
) : UserDataSource {

    private val userCollection = firestore.collection(
        Configuration.FirebaseModel.USER_ENTITY)

    override suspend fun upsertUser(user: UserCollection) {
        withContext(Dispatchers.IO) {
            userCollection.document(user.uid).set(user)
        }
    }

    override suspend fun deleteUser(user: UserCollection) {
        withContext(Dispatchers.IO) {
            userCollection.document(user.uid).delete()
        }
    }

    override suspend fun getUser(userId: String): UserCollection? {
        return withContext(Dispatchers.IO) {
            try {
                userCollection.document(userId).get().await().toObject<UserCollection>()
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get user: ${e.message}"
                )
                throw e
            }
        }
    }

    override suspend fun getParticipants(userIdList: List<String>): Flow<List<UserCollection>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = userCollection.whereIn("uid", userIdList).orderBy(
                    "username", Query.Direction.ASCENDING
                ).snapshots()

                querySnapshot.map { it.toObjects<UserCollection>() }
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get all user: ${e.message}"
                )
                throw e
            }
        }
    }

    override suspend fun getAllUsersOrderedByName(): Flow<List<UserCollection>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = userCollection.orderBy(
                    "username", Query.Direction.ASCENDING
                ).snapshots()

                querySnapshot.map { it.toObjects<UserCollection>() }
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get all user: ${e.message}"
                )
                throw e
            }
        }

    }

    override suspend fun searchUsers(usernameEmail: String): Flow<List<UserCollection>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = userCollection.where(
                    Filter.arrayContains(
                        "username", usernameEmail
                    )
                ).where(
                    Filter.arrayContains(
                        "email", usernameEmail
                    )
                ).orderBy(
                    "username", Query.Direction.ASCENDING
                ).snapshots()

                querySnapshot.map { it.toObjects<UserCollection>() }
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get all user: ${e.message}"
                )
                throw e
            }

        }
    }
}
