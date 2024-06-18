package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull


data class ChatUseCases(
    val upsertChat: UpsertChat,
    val upsertMessage: UpsertMessage,
    val deleteChat: DeleteChat,
    val getChat: GetChat,
    val getAllChatsOrderedByName: GetAllChatsOrderedByName,
    val searchChats: SearchChats
)

class UpsertMessage(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(message: Message) {
        val chat = chatRepository.getChat(message.chatId).firstOrNull()!!
        chatRepository.upsertChat(chat.copy(lastMessage = message))
        chatRepository.upsertMessage(message)
    }
}