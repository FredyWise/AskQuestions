package com.fredy.askquestions.features.domain.usecases.UserUseCases

import com.fredy.askquestions.features.domain.models.User
import com.fredy.mysavings.Feature.Domain.Repository.UserRepository

class UpdateUser(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        userRepository.upsertUser(user)
    }
}