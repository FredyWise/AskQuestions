package com.fredy.askquestions.features.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fredy.askquestions.features.data.database.firebase.models.MessageCollection
import com.fredy.askquestions.features.data.mappers.toMessageMap
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last

class MessagePagingSource (
    private val chatRepository: ChatRepository,
    private val chatId: String,
) : PagingSource<Timestamp, MessageCollection>() {
    override fun getRefreshKey(state: PagingState<Timestamp, MessageCollection>): Timestamp? {
        return null
    }

    override suspend fun load(params: LoadParams<Timestamp>): LoadResult<Timestamp, MessageCollection> {
        return try {
            val currentPage = chatRepository.getAllMessagesInTheSameChat(chatId,params.key).first()
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
