package com.fredy.askquestions


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.fredy.askquestions.features.data.apis.ApiConfiguration
import com.fredy.askquestions.features.ui.screens.chatScreen.ChatScreen
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.ChatViewModel
import com.fredy.askquestions.ui.theme.AskQuestionsTheme
import com.google.ai.client.generativeai.GenerativeModel

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = ApiConfiguration.GeminiModel.API_KEY
        )

        enableEdgeToEdge()
        setContent {
            AskQuestionsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: ChatViewModel = hiltViewModel()
                    ChatScreen(modifier = Modifier.padding(
                        innerPadding
                    ),
                        state = viewModel.state,
//                        onPhotoPicked = viewModel::onImageSelected,
                        onTextChange = viewModel::onTextChange,
                        messages = emptyList(),
                        onClick = {
                            viewModel.onSuggestClick(
                                generativeModel
                            )
                        })
                }

            }
        }
    }
}

