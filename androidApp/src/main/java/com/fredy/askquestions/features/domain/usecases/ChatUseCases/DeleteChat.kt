package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.repositories.ChatRepository

class DeleteChat(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chat: Chat) {
        chatRepository.deleteChat(chat)
    }
}