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
        val messageCollection = message.toMessageCollection()
        val chatId = chatRepository.upsertChat(chat.updateLastMessage(messageCollection))
        Timber.d("Upsert message: $messageCollection")
        chatRepository.upsertMessage(
            messageCollection.copy(chatId = chatId)
        )
        return chatId
    }
}