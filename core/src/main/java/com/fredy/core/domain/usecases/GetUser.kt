package com.fredy.core.domain.usecases

import com.fredy.core.domain.models.User
import com.fredy.core.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUser(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: String): Flow<User?> {
        return userRepository.getUser(userId)
    }
}