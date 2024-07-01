package com.fredy.askquestions.features.domain.usecases.ChatUseCases

import com.fredy.askquestions.features.data.database.firebase.models.MessageCollection
import com.fredy.askquestions.features.domain.repositories.ChatRepository

class DeleteMessage(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(messageCollection: MessageCollection) {
        chatRepository.deleteMessage(messageCollection)
    }
}