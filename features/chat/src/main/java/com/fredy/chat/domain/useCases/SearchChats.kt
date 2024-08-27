package com.fredy.chat.domain.useCases

import com.fredy.chat.domain.ChatRepository
import com.fredy.core.domain.models.Chat
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