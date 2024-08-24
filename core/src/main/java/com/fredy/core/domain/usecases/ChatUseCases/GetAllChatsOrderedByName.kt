package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetAllChatsOrderedByName(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Chat>> {
        return chatRepository.getAllChatsOrderedByName()
    }
}