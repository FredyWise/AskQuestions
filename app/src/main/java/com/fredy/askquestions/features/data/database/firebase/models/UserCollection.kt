package com.fredy.askquestions.features.data.database.firebase.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserCollection(
    @PrimaryKey
    val uid: String = "",
    val username: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val notificationKey: String? = null,
    val profilePictureUrl: String? = null,
    val selected: Boolean = false
)