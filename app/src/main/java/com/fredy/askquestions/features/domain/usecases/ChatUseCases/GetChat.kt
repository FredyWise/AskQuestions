package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetChat(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<Chat?> {
        return chatRepository.getChat(chatId)
    }
}