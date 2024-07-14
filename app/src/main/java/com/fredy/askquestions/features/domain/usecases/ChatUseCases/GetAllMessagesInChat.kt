package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.fredy.askquestions.features.data.Util.Configuration
import com.fredy.askquestions.features.data.database.room.ChattingDatabase
import com.fredy.askquestions.features.data.mappers.toMessage
import com.fredy.askquestions.features.data.pagingSource.MessageRemoteMediator
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.fredy.askquestions.features.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetAllMessagesInChat(
    private val chattingDatabase: ChattingDatabase,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) {

    @OptIn(ExperimentalPagingApi::class)
    operator fun invoke(chatId: String): Flow<PagingData<Message>> = flow {
        Timber.d("UseCase.GetAllMessagesInChat.Start")
        val currentUser = userRepository.getCurrentUser()!!
        Pager(
            config = PagingConfig(pageSize = Configuration.UIConfig.MessageLimit),
            remoteMediator = MessageRemoteMediator(
                chattingDatabase,
                chatRepository,
                chatId
            ),
            pagingSourceFactory = {
                chattingDatabase.messageDao.getPagingSource(
                    chatId
                )
//            MessagePagingSource(
//                chatRepository,
//                chatId
//            )
            },
        ).flow.collect { pagingData ->
            emit(pagingData.map {
                Timber.e("message: $it")
                it.toMessage(
                    currentUser.uid
                )
            })
            Timber.d("UseCase.GetAllMessagesInChat.Finish")
        }
    }
}