package com.fredy.chat.viewmodels

sealed interface MessageEvent {
    data class OnTextChange(val text: String) : MessageEvent
    object OnSendClick : MessageEvent
}
