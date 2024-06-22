package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.domain.mapper.toMessageMap
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.models.MessageMap
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.fredy.askquestions.features.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetAllMessagesInChat(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(chatId: String): Flow<List<MessageMap>> = flow {
        val currentUser = userRepository.getCurrentUser()!!
        chatRepository.getAllMessagesInTheSameChat(chatId).collect{
            val messageMaps = it.map { it.toMessageMap(currentUser.uid) }
            emit(messageMaps)
        }
    }.catch {
//        emit(emptyList())
    }
}
