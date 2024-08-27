package com.fredy.core.domain.usecases

import com.fredy.core.domain.models.User
import com.fredy.core.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class GetAllUsersOrderedByName(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsersOrderedByName()
    }
}