package com.fredy.askquestions

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI

class MainViewModel(): ViewModel() {
    var state by mutableStateOf(MainState())
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

class MainViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class MainState(
    val isLoading: Boolean = false,
    val response: GenerateContentResponse? = null,
    val text: String = "",
    val inputText: String = ""
)