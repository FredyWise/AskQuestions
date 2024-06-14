package com.fredy.askquestions

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.fredy.askquestions.ui.theme.AskQuestionsTheme
import com.google.ai.client.generativeai.GenerativeModel
import com.simpleseries.businesscalculator.SimpleBusinessCalculator


import java.net.URI

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.apiKey
        )

        enableEdgeToEdge()
        setContent {
            AskQuestionsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: MainViewModel by viewModels {
                        MainViewModelFactory()
                    }
                    MainScreen(
                        modifier = Modifier.padding(
                            innerPadding
                        ),
                        state = viewModel.state,
//                        onPhotoPicked = viewModel::onImageSelected,
                        onTextChange = viewModel::onTextChange,
                        onClick = {viewModel.onSuggestClick(generativeModel)}
                    )
                    SimpleBusinessCalculator()
                }

            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainState,
    onTextChange: (String) -> Unit,
    onClick: () -> Unit,
) {
    Column(modifier = modifier) {
        // Display area for responses
        Text(text = state.text)

        // Input field
        OutlinedTextField(
            value = state.inputText,
            onValueChange = { onTextChange(it) },
            label = { Text("Type your message") },
            modifier = Modifier.fillMaxWidth()
        )

        // Send button
        Button(
            onClick = onClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Send")
        }
    }
}
