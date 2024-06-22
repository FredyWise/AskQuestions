package com.fredy.askquestions.features.ui.viewmodels.ChatViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.ChatUseCases
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    private val model: GenerativeModel,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(
        MessageState()
    )

    private val _chatId = MutableStateFlow("")

    init {
        viewModelScope.launch {
            savedStateHandle.get<String>("chatId")?.let { chatId ->
                _chatId.update { chatId }
                chatUseCases.getChat(chatId).collect { chat ->
                    chat?.let { chat ->
                        _state.update {
                            it.copy(
                                currentChat = chat
                            )
                        }
                    }
                }
                onEvent(MessageEvent.LoadMessages)
            }
        }
    }

    private val _messageList = _chatId.flatMapLatest {
        chatUseCases.getAllMessagesInChat(it).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )
    }

    val state = combine(
        _state,
        _messageList
    ) { state, messageList ->
        Timber.e("state: $state")
        Timber.e("messageList: $messageList")
        state.copy(
            messages = messageList
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        _state.value
    )


    fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.OnTextChange -> handleTextChange(
                event.text
            )

            MessageEvent.OnSendClick -> {
                if (_state.value.inputText.isBlank() || _state.value.isLoading) return

                viewModelScope.launch(Dispatchers.IO) {
                    val newMessage = Message(
                        text = _state.value.inputText,
                    )

                    try {
                        val chatId = chatUseCases.upsertMessage(
                            _state.value.currentChat,
                            newMessage
                        )
                        _state.update {
                            it.copy(
                                currentChat = it.currentChat.copy(
                                    chatId = chatId
                                ),
                                messages = it.messages + newMessage,
                                inputText = ""
                            )
                        }
                    } catch (e: Exception) {
                        // Handle error (e.g., logging, showing a toast, etc.)
                    }
                }
            }
//            MessageEvent.SuggestContent -> handleSuggestContent()
            MessageEvent.SuggestContent -> {}
            MessageEvent.LoadMessages -> {
//                viewModelScope.launch {
//                    chatUseCases.getAllMessagesInChat(
//                        _state.value.currentChat.chatId
//                    ).collect { messages ->
//                        _messageList.update { messages }
//                    }
//                }
            }
        }
    }

    private fun handleTextChange(text: String) {
        _state.update { it.copy(inputText = text) }
    }


//    private fun handleSuggestContent() {
//        if (state.inputText.isBlank() || state.isLoading) return
//
//        viewModelScope.launch(Dispatchers.IO) {
//            state = state.copy(isLoading = true)
//            try {
//                val response = model.generateContent(
//                    content {
//                        text(state.inputText)
//                    })
//
//                // Handle generated response and update messages
//                val suggestedMessage = Message(
//                    text = response.text ?: "Something went wrong",
//                )
//
//                chatRepository.upsertMessage(
//                    suggestedMessage
//                ) // Save the suggested message
//
//                state = state.copy(
//                    response = response,
//                    text = suggestedMessage.text,
//                    isLoading = false,
//                    messages = state.messages + suggestedMessage // Add the suggested message to the list
//                )
//            } catch (e: Exception) {
//                // Handle error (e.g., logging, showing a toast, etc.)
//                state = state.copy(isLoading = false)
//            }
//        }
//    }
//
//    fun onSuggestClick() {
//        if (state.inputText == "" || state.isLoading) return
//
//        viewModelScope.launch(Dispatchers.IO) {
//            state = state.copy(isLoading = true)
//            val response = model.generateContent(
//                content {
//                    text(state.inputText)
//                })
//
//            state = state.copy(
//                response = response,
//                text = response.text ?: "Something went wrong",
//                isLoading = false
//            )
//        }
//
//    }
}


data class MessageState(
    val currentChat: Chat = Chat(),
    val isLoading: Boolean = false,
    val currentUserId: String = "",
    val response: GenerateContentResponse? = null,
    val text: String = "",
    val inputText: String = "",
    val messages: List<Message> = emptyList() // List to hold chat messages
)
