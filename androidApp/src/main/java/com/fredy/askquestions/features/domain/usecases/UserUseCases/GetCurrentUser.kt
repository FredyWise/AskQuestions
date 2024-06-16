package com.fredy.askquestions.features.domain.usecases.UserUseCases

import com.fredy.askquestions.features.domain.models.User
import com.fredy.askquestions.features.domain.util.Resource.DataError
import com.fredy.askquestions.features.domain.util.Resource.Resource
import com.fredy.mysavings.Feature.Domain.Repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUser(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Resource<User?, DataError.Database>> {
        return userRepository.getCurrentUserFlow()
    }
}