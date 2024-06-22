package com.fredy.askquestions.features.domain.usecases.ChatUseCases


data class ChatUseCases(
    val upsertChat: UpsertChat,// useless
    val upsertMessage: UpsertMessage,
    val deleteChat: DeleteChat,
    val deleteMessage: DeleteMessage,
    val getChat: GetChat,
    val getAllChatsOrderedByName: GetAllChatsOrderedByName,
    val getAllMessagesInChat: GetAllMessagesInChat,
//    val searchMessages: SearchMessages,
    val searchChats: SearchChats
)

