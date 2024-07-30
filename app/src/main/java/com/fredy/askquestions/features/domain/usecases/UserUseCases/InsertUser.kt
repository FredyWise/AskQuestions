package com.fredy.askquestions.features.domain.usecases.UserUseCases

import android.net.Uri
import com.fredy.askquestions.features.data.mappers.toUser
import com.fredy.askquestions.features.domain.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class InsertUser(
    private val userRepository: UserRepository,
    private val firebaseMessaging: FirebaseMessaging,
) {
    suspend operator fun invoke(
        firebaseUser: FirebaseUser?,
        photoUri: Uri = Uri.EMPTY
    ) {
        firebaseUser?.run {
            val profilePictureUrl = if (photoUri != Uri.EMPTY) {
                UploadProfilePicture().invoke(
                    uid, photoUri
                )
            } else {
                photoUrl.toString()
            }
            var token: String? = null
            firebaseMessaging.token.addOnSuccessListener {
                Timber.d("Token: $it")
                token = it
            }.await()
            Timber.d("Token: $token")
            val user = this.toUser().copy(
                notificationKey = token,
                profilePictureUrl = profilePictureUrl
            )

            val isExist = userRepository.getUser(
                user.uid
            ).firstOrNull()
            Timber.i("InsertUser.Data: $isExist")
            if (isExist == null) {
                userRepository.upsertUser(user)
            }
        }
    }


}