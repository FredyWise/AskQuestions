package com.fredy.core.domain.usecases

import com.fredy.core.domain.models.User

import com.fredy.core.domain.repositories.UserRepository
import com.fredy.core.util.resource.DataError
import com.fredy.core.util.resource.Resource
import kotlinx.coroutines.flow.Flow

class GetCurrentUser(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Resource<User?, DataError.Database>> {
        return userRepository.getCurrentUserFlow()
    }
}