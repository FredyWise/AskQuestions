package com.fredy.core.data.database.firebase.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserCollection(
    val uid: String = "",
    val username: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val notificationKey: String? = null,
    val profilePictureUrl: String? = null,
)