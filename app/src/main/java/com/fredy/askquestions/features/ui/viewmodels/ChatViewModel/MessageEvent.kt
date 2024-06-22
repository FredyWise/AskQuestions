package com.fredy.askquestions.features.ui.viewmodels.ChatViewModel

sealed interface MessageEvent {
    data class OnTextChange(val text: String) : MessageEvent
    object OnSendClick : MessageEvent
    object LoadMessages : MessageEvent
    object SuggestContent : MessageEvent
}
