package com.fredy.chat.domain.useCases

import com.fredy.chat.domain.ChatRepository
import com.fredy.core.domain.models.Message

class DeleteMessage(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(message: Message) {
        chatRepository.deleteMessage(message)
    }
}