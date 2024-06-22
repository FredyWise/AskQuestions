package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository

class DeleteMessage(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(message: Message) {
        chatRepository.deleteMessage(message)
    }
}