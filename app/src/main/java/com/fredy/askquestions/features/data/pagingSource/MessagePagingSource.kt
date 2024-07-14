package com.fredy.askquestions.features.data.pagingSource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fredy.askquestions.features.data.database.firebase.models.MessageCollection
import com.fredy.askquestions.features.data.database.room.ChattingDatabase
import com.fredy.askquestions.features.data.database.room.models.MessageEntity
import com.fredy.askquestions.features.data.mappers.toMessageEntity
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.io.IOException

class MessagePagingSource(
    private val chatRepository: ChatRepository,
    private val chatId: String,
): PagingSource<Timestamp, MessageCollection>() {
//    override fun getRefreshKey(state: PagingState<Timestamp, MessageCollection>): Timestamp? {
//        return null
//    }
    override fun getRefreshKey(state: PagingState<Timestamp, MessageCollection>): Timestamp? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { anchorPage ->
                val pageIndex = state.pages.indexOf(anchorPage)
                if (pageIndex == 0) {
                    null
                } else {
                    state.pages[pageIndex - 1].nextKey
                }
            }
        }
    }
    override suspend fun load(params: LoadParams<Timestamp>): LoadResult<Timestamp, MessageCollection> {
        return try {
//            lateinit var result: LoadResult<Timestamp, MessageCollection>
//
//            chatRepository.getAllMessagesInTheSameChat(
//                chatId,
//                params.key
//            ).collect { currentPage ->
//                val nextPage = currentPage.last().timestamp
//                result = LoadResult.Page(
//                    data = currentPage,
//                    prevKey = null,
//                    nextKey = nextPage
//                )
//            }
//            result
            val currentPage = chatRepository.getAllMessagesInTheSameChat(
                chatId,
                params.key
            ).first()
            val nextPage = currentPage.last().timestamp
            LoadResult.Page(
                data = currentPage,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator(
    private val chattingDb: ChattingDatabase,
    private val chatRepository: ChatRepository,
    private val chatId: String,
): RemoteMediator<Int, MessageEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    state.lastItemOrNull()?.timestamp
                }
            }

            val messages = chatRepository.getAllMessagesInTheSameChat(
                chatId,
                loadKey,
                state.config.pageSize
            ).first()

            chattingDb.withTransaction {
//                if (loadType == LoadType.REFRESH) { //this is used if when using local id (remote id and local id different)
//                    chattingDb.messageDao.clearAllMessages()
//                }
                val messageEntities = messages.map { it.toMessageEntity() }
                chattingDb.messageDao.upsertAllMessages(
                    messageEntities
                )
            }

            MediatorResult.Success(
                endOfPaginationReached = messages.isEmpty()
            )

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
