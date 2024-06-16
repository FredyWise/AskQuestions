package com.fredy.askquestions.features.domain.usecases.UserUseCases

import com.fredy.askquestions.features.domain.models.User
import com.fredy.askquestions.features.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUser(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: String): Flow<User?> {
        return userRepository.getUser(userId)
    }
}