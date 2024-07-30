package com.fredy.askquestions.features.data.mappers

import com.fredy.askquestions.features.domain.models.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid,
        username = displayName,
        email = email,
        phone = phoneNumber,
        profilePictureUrl = photoUrl.toString(),
    )
}