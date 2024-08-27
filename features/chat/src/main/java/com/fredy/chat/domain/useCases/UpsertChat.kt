package com.fredy.chat.domain.useCases

import com.fredy.chat.domain.ChatRepository
import com.fredy.core.domain.models.Chat

class UpsertChat(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chat: Chat):String {
        return chatRepository.upsertChat(chat)
    }
}