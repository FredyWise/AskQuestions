package com.fredy.askquestions


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.fredy.askquestions.features.ui.navigation.Graph
import com.fredy.askquestions.features.ui.navigation.RootNavGraph
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthEvent
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthViewModel
import com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel.PreferencesViewModel
import com.fredy.askquestions.ui.theme.AskQuestionsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private val viewModel by viewModels<PreferencesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val setting by viewModel.state.collectAsStateWithLifecycle()
            val state by authViewModel.state.collectAsStateWithLifecycle()

            if (setting.updated) {
                if (!setting.autoLogin) {
                    authViewModel.onEvent(
                        AuthEvent.SignOut
                    )
                }
                AskQuestionsTheme(state = setting) {
                    val navController = rememberNavController()
                    val startDestination = if (state.signedInUser != null && setting.autoLogin && !setting.bioAuth) Graph.MainNav else Graph.AuthNav
                    RootNavGraph(
                        navController,
                        startDestination,
                        viewModel,
                        authViewModel
                    )
                }

            }

        }
    }
}

