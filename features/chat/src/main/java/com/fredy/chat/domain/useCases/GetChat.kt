package com.fredy.chat.domain.useCases

import com.fredy.chat.domain.ChatRepository
import com.fredy.core.domain.models.Chat
import kotlinx.coroutines.flow.Flow

class GetChat(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<Chat?> {
        return chatRepository.getChat(chatId)
    }
}