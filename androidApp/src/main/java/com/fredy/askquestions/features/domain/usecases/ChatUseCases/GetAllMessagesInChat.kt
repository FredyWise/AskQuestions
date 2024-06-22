package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetAllMessagesInChat(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>> = flow {
        chatRepository.getAllMessagesInTheSameChat(chatId).collect{
            Timber.e("getAllMessagesInChat: $it")
            emit(it)
        }
    }.catch {
//        emit(emptyList())
    }
}
