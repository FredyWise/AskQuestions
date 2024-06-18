package com.fredy.askquestions.features.ui.viewmodels.ChatViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fredy.askquestions.features.domain.models.Chat
import com.fredy.askquestions.features.domain.models.Message
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val model: GenerativeModel
): ViewModel() {
    var state by mutableStateOf(ChatState())
        private set

    init {
        onEvent(MessageEvent.LoadMessages)
    }

    fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.OnTextChange -> handleTextChange(
                event.text
            )

            MessageEvent.OnSendClick -> handleSendClick()
            MessageEvent.LoadMessages -> handleLoadMessages()
            MessageEvent.SuggestContent -> handleSuggestContent()
        }
    }

    private fun handleTextChange(text: String) {
        state = state.copy(inputText = text)
    }

    private fun handleSendClick() {
        if (state.inputText.isBlank() || state.isLoading) return

        viewModelScope.launch(Dispatchers.IO) {
            val newMessage = Message(
                text = state.inputText,
            )

            try {
                chatRepository.upsertMessage(
                    newMessage
                ) // Save the message to the repository
                state = state.copy(
                    messages = state.messages + newMessage, // Add the new message to the list
                    inputText = "" // Clear the input field
                )
            } catch (e: Exception) {
                // Handle error (e.g., logging, showing a toast, etc.)
            }
        }
    }

    private fun handleLoadMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                chatRepository.getAllMessagesInTheSameChat(
                    state.currentChat.chatId
                ).collect { messages ->
                    state = state.copy(messages = messages)
                }
            } catch (e: Exception) {
                // Handle error (e.g., logging, showing a toast, etc.)
            }
        }
    }

    private fun handleSuggestContent() {
        if (state.inputText.isBlank() || state.isLoading) return

        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isLoading = true)
            try {
                val response = model.generateContent(
                    content {
                        text(state.inputText)
                    })

                // Handle generated response and update messages
                val suggestedMessage = Message(
                    text = response.text ?: "Something went wrong",
                )

                chatRepository.upsertMessage(
                    suggestedMessage
                ) // Save the suggested message

                state = state.copy(
                    response = response,
                    text = suggestedMessage.text,
                    isLoading = false,
                    messages = state.messages + suggestedMessage // Add the suggested message to the list
                )
            } catch (e: Exception) {
                // Handle error (e.g., logging, showing a toast, etc.)
                state = state.copy(isLoading = false)
            }
        }
    }

    fun onSuggestClick() {
        if (state.inputText == "" || state.isLoading) return

        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isLoading = true)
            val response = model.generateContent(
                content {
                    text(state.inputText)
                })

            state = state.copy(
                response = response,
                text = response.text ?: "Something went wrong",
                isLoading = false
            )
        }

    }
}

//class MainViewModelFactory() : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(
//                ChatViewModel::class.java)) {
//            return ChatViewModel() as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}


data class ChatState(
    val currentChat: Chat = Chat(),
    val isLoading: Boolean = false,
    val currentUserId: String = "",
    val response: GenerateContentResponse? = null,
    val text: String = "",
    val inputText: String = "",
    val messages: List<Message> = emptyList() // List to hold chat messages
)
