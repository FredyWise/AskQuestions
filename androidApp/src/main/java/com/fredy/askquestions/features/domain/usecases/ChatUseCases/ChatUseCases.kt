package com.fredy.askquestions.features.domain.usecases.ChatUseCases


data class ChatUseCases(
    val insertChat: InsertChat,
    val deleteChat: DeleteChat,
    val getChat: GetChat,
    val getAllChatsOrderedByName: GetAllChatsOrderedByName,
    val searchChats: SearchChats
)



