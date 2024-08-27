package com.fredy.chat.domain.useCases

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.fredy.chat.data.MessageRemoteMediator
import com.fredy.core.data.database.room.ChattingDatabase
import com.fredy.chat.data.mappers.toMessage
import com.fredy.chat.domain.ChatRepository
import com.fredy.core.data.database.room.dao.MessageDao
import com.fredy.core.data.util.Configuration
import com.fredy.core.domain.models.Message
import com.fredy.core.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetAllMessagesInChat(
    private val chattingDatabase: ChattingDatabase,
    private val messageDao: MessageDao,
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
                messageDao.getPagingSource(
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