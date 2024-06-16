package com.fredy.askquestions.features.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(model: GenerativeModel): ViewModel() {
    var state by mutableStateOf(ChatState())
        private set
//
//    fun onImageSelected(uri: Uri) {
//        viewModelScope.launch(Dispatchers.IO) {
//            state = state.copy(selectedImageBitmap = uriReader.readAsBitmap(uri))
//        }
//    }

    fun onTextChange(text: String){
        viewModelScope.launch {
            state = state.copy(inputText = text)
        }
    }

    fun onSuggestClick(model: GenerativeModel) {
        if (state.inputText == "" || state.isLoading) return

        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isLoading = true)
            val response = model.generateContent(
                content {
                    text(state.inputText)
                }
            )

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
    val isLoading: Boolean = false,
    val currentUserId: String = "",
    val response: GenerateContentResponse? = null,
    val text: String = "",
    val inputText: String = ""
)