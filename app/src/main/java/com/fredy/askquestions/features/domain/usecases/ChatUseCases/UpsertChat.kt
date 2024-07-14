package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.repositories.ChatRepository

class UpsertChat(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chat: Chat):String {
        return chatRepository.upsertChat(chat)
    }
}