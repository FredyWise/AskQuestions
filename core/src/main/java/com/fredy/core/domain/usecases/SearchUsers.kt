package com.fredy.core.domain.usecases

import com.fredy.core.domain.models.User
import com.fredy.core.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class SearchUsers(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(usernameEmail: String): Flow<List<User>> {
        return userRepository.searchUsers(
            usernameEmail
        )
    }
}