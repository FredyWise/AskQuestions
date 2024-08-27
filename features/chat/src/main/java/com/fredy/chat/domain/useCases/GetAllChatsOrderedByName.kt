package com.fredy.chat.domain.useCases

import com.fredy.chat.domain.ChatRepository
import com.fredy.core.domain.models.Chat
import kotlinx.coroutines.flow.Flow

class GetAllChatsOrderedByName(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Chat>> {
        return chatRepository.getAllChatsOrderedByName()
    }
}