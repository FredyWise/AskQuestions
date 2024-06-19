package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetAllMessagesInChat(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId:String): Flow<List<Message>> {
        return chatRepository.getAllMessagesInTheSameChat(chatId)
    }
}