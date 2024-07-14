package com.fredy.askquestions.features.ui.viewmodels.ChatViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertHeaderItem

import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.ChatUseCases
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
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
    private val _uiState = MutableStateFlow(
        UIState()
    )

    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            savedStateHandle.get<String>("chatId")?.let { chatId ->
                chatUseCases.getChat(chatId).collect { chat ->
                    chat?.let { chat ->
                        _state.update {
                            it.copy(
                                currentChat = chat,
                                updateChat = it.updateChat.not()
                            )
                        }
                    }
                }
            }
        }
    }

//    private val _newMessageList = _state.flatMapLatest {
//        chatUseCases.getNewMessagesInChat(it.currentChat.chatId).stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(),
//            PagingData.empty()
//        )
//    }

    private val _messageList = _state.flatMapLatest {
        chatUseCases.getAllMessagesInChat(it.currentChat.chatId).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            PagingData.empty()
        )
    }

    val message = _messageList


    fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.OnTextChange -> {
                _uiState.update {
                    it.copy(
                        inputText = event.text
                    )
                }
            }

            MessageEvent.OnSendClick -> {
                val inputText = _uiState.value.inputText
                if (inputText.isBlank() || _uiState.value.isLoading) return

                _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }

                viewModelScope.launch(Dispatchers.IO) {
                    val newMessage = Message(
                        text = inputText,
                    )
                    handleSendMessages(
                        newMessage
                    )

                    if (inputText.first() == '/') {
                        handleSendMessages(
                            handleSuggestContent(
                                inputText.removePrefix(
                                    "/"
                                )
                            )
                        )
                    }
                }
            }

        }
    }


    private suspend fun handleSuggestContent(
        inputText: String
    ): Message {
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
            _uiState.update {
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
//                    updateChat = it.updateChat.not()
                )
            }
            _uiState.update {
                it.copy(
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
    val updateChat: Boolean = false,
)

data class UIState(
    val isLoading: Boolean = false,
    val text: String = "",
    val inputText: String = "",
)

