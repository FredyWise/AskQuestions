package com.fredy.core.domain.models

data class User(
    val uid: String = "",
    val username: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val notificationKey: String? = null,
    val profilePictureUrl: String? = null,
)