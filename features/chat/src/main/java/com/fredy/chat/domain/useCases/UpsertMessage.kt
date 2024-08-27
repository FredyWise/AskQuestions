package com.fredy.chat.domain.useCases

import com.fredy.chat.domain.ChatRepository
import com.fredy.core.domain.models.Chat
import com.fredy.core.domain.models.Message
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