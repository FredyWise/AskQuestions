package com.fredy.askquestions.features.ui.viewmodels.ChatViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.models.MessageMap
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.ChatUseCases
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
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
        _state, _messageList
    ) { state, messageList ->
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
            is MessageEvent.OnTextChange -> {
                _state.update { it.copy(inputText = event.text) }
            }

            MessageEvent.OnSendClick -> {
                val inputText=_state.value.inputText
                if (inputText.isBlank() || _state.value.isLoading) return

                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }

                viewModelScope.launch(Dispatchers.IO) {
                    val newMessage = Message(
                        text = inputText,
                    )
                    handleSendMessages(newMessage)

                    if (inputText.first() == '/') {
                        handleSendMessages(
                            handleSuggestContent(inputText.removePrefix("/"))
                        )
                    }

                }
            }

        }
    }


    private suspend fun handleSuggestContent(inputText: String): Message {
        val suggestedMessage = try {
            val response = model.generateContent(
                content {
                    text(inputText)
                })

            Message(
                senderId = "Model",
                text = response.text ?: "Something went wrong",
            )
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false
                )
            }

            Message(
                senderId = "Model",
                text = "Something went wrong",
            )
        }

        return suggestedMessage
    }

    private suspend fun handleSendMessages(
        newMessage: Message
    ) {
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
                    inputText = "",
                    isLoading = false,
                )
            }
        } catch (e: Exception) {
            // Handle error (e.g., logging, showing a toast, etc.)
        }
    }


}


data class MessageState(
    val currentChat: Chat = Chat(),
    val isLoading: Boolean = false,
    val currentUserId: String = "",
    val response: GenerateContentResponse? = null,
    val text: String = "",
    val inputText: String = "",
    val messages: List<MessageMap> = emptyList() // List to hold chat messages
)
