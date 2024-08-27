package com.fredy.core.domain.usecases

import com.fredy.core.domain.models.User
import com.fredy.core.domain.repositories.UserRepository

class DeleteUser(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        userRepository.deleteUser(user)
    }
}