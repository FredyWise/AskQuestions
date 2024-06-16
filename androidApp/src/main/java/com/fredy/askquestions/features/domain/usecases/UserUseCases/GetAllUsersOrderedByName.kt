package com.fredy.mysavings.Feature.Domain.UseCases.UserUseCases

import com.fredy.askquestions.features.domain.models.User
import com.fredy.mysavings.Feature.Domain.Repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetAllUsersOrderedByName(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsersOrderedByName()
    }
}