package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.data.mappers.toMessageCollection
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import timber.log.Timber

class UpsertMessage(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chat: Chat, message: Message) : String{
        val chatId = chatRepository.upsertChat(chat.updateLastMessage(message))
        Timber.d("Upsert message: $message")
        chatRepository.upsertMessage(
            message.copy(chatId = chatId)
        )
        return chatId
    }
}