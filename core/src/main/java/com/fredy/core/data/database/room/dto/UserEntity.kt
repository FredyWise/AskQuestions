package com.fredy.core.data.database.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    val uid: String = "",
    val username: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val notificationKey: String? = null,
    val profilePictureUrl: String? = null,
)