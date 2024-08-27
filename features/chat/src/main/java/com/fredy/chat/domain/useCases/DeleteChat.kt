package com.fredy.chat.domain.useCases

import com.fredy.chat.domain.ChatRepository
import com.fredy.core.domain.models.Chat

class DeleteChat(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chat: Chat) {
        chatRepository.deleteChat(chat)
    }
}

