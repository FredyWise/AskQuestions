package com.fredy.askquestions.features.data.mappers

import com.fredy.askquestions.features.data.database.firebase.dto.UserCollection
import com.fredy.askquestions.features.data.database.room.dto.UserEntity
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

fun UserCollection.toUser(): User {
    return User(
        uid = uid,
        username = username,
        phone = phone,
        email = email,
        notificationKey = notificationKey,
        profilePictureUrl = profilePictureUrl,
    )
}

fun UserCollection.toUserEntity(): UserEntity {
    return UserEntity(
        uid = uid,
        username = username,
        phone = phone,
        email = email,
        notificationKey = notificationKey,
        profilePictureUrl = profilePictureUrl,
    )
}

fun UserEntity.toUser(): User {
    return User(
        uid = uid,
        username = username,
        phone = phone,
        email = email,
        notificationKey = notificationKey,
        profilePictureUrl = profilePictureUrl,
    )
}

fun User.toUserCollection(): UserCollection {
    return UserCollection(
        uid = uid,
        username = username,
        phone = phone,
        email = email,
        notificationKey = notificationKey,
        profilePictureUrl = profilePictureUrl,
    )
}
fun User.toUserEntity(): UserEntity {
    return UserEntity(
        uid = uid,
        username = username,
        phone = phone,
        email = email,
        notificationKey = notificationKey,
        profilePictureUrl = profilePictureUrl,
    )
}