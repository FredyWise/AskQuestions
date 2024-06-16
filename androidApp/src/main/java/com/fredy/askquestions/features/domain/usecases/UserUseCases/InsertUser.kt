package com.fredy.askquestions.features.domain.usecases.UserUseCases

import com.fredy.askquestions.features.domain.models.User
import com.fredy.mysavings.Feature.Domain.Repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

class InsertUser(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        val isExist = userRepository.getUser(user.uid).firstOrNull()
        Timber.i("InsertUser.Data: $isExist")
        if (!isExist.isNotNull()) {
            userRepository.upsertUser(user)
        }
    }
}