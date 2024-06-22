package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow

class SearchChats(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatName: String): Flow<List<Chat>> {
        return chatRepository.searchChats(
            chatName
        )
    }
}