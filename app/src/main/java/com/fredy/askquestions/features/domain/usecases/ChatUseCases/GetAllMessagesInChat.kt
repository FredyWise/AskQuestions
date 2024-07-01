package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.fredy.askquestions.features.data.mappers.toMessageMap
import com.fredy.askquestions.features.data.pagingSource.MessagePagingSource
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.fredy.askquestions.features.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllMessagesInChat(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(chatId: String): Flow<PagingData<Message>> = flow {
        val currentUser = userRepository.getCurrentUser()!!
        Pager(
            PagingConfig(
                pageSize = 20
            )
        ) {
            MessagePagingSource(
                chatRepository,
                chatId
            )
        }.flow.collect{
            emit(it.map { it.toMessageMap(currentUser.uid) })
        }
    }
}
