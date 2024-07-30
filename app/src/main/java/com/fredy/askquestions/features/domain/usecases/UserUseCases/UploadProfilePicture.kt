package com.fredy.askquestions.features.domain.usecases.UserUseCases

import android.net.Uri
import com.fredy.askquestions.features.domain.repositories.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class UploadProfilePicture {
    suspend operator fun invoke(
        uid: String, imageUri: Uri
    ): String {
        return try {
            val storageRef = Firebase.storage.reference
            val profilePictureRef = storageRef.child(
                "profile_pictures/$uid.jpg"
            )
            val downloadUri = profilePictureRef.putFile(
                imageUri
            ).await().storage.downloadUrl.await()
            downloadUri.toString()
        } catch (e: Exception) {
            throw e
        }
    }
}