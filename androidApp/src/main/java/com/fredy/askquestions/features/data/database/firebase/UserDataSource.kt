package com.fredy.askquestions.features.data.database.firebase


import com.fredy.askquestions.features.data.apis.ApiConfiguration
import com.fredy.askquestions.features.domain.models.User
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
    suspend fun upsertUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun getAllUsersOrderedByName(): Flow<List<User>>
    suspend fun searchUsers(usernameEmail: String): Flow<List<User>>
}

class UserDataSourceImpl(
    private val firestore: FirebaseFirestore,
) : UserDataSource {

    private val userCollection = firestore.collection(ApiConfiguration.FirebaseModel.USER_ENTITY)

    override suspend fun upsertUser(user: User) {
        withContext(Dispatchers.IO) {
            userCollection.document(user.uid).set(user)
        }
    }

    override suspend fun deleteUser(user: User) {
        withContext(Dispatchers.IO) {
            userCollection.document(user.uid).delete()
        }
    }

    override suspend fun getUser(userId: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                userCollection.document(userId).get().await().toObject<User>()
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get user: ${e.message}"
                )
                throw e
            }
        }

    }

    override suspend fun getAllUsersOrderedByName(): Flow<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = userCollection.orderBy(
                    "username", Query.Direction.ASCENDING
                ).snapshots()

                querySnapshot.map { it.toObjects<User>() }
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get all user: $e"
                )
                throw e
            }
        }

    }

    override suspend fun searchUsers(usernameEmail: String): Flow<List<User>> {
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

                querySnapshot.map { it.toObjects<User>() }
            } catch (e: Exception) {
                Timber.e(
                    "Failed to get all user: $e"
                )
                throw e
            }

        }
    }
}
