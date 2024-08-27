package com.fredy.core.domain.usecases

import com.fredy.core.domain.models.User
import com.fredy.core.domain.repositories.UserRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class UpdateUser(
    private val userRepository: UserRepository,
    private val firebaseMessaging: FirebaseMessaging
) {
    suspend operator fun invoke(user: User) {
        var token: String? = null
        firebaseMessaging.token.addOnSuccessListener {
            Timber.d("Token: $it")
            token = it
        }.await()
        Timber.d("Token: $token")
        userRepository.upsertUser(user.copy(notificationKey = token))
    }
}