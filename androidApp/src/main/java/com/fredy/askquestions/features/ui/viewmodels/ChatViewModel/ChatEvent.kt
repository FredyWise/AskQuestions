package com.fredy.askquestions.features.ui.viewmodels.ChatViewModel

sealed interface ChatEvent {
    data class OnTextChange(val text: String) : ChatEvent
    object OnSendClick : ChatEvent
    object LoadMessages : ChatEvent
    object SuggestContent : ChatEvent
}
