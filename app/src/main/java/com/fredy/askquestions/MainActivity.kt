package com.fredy.askquestions


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.fredy.askquestions.features.data.enums.DisplayMode
import com.fredy.askquestions.features.ui.navigation.Graph
import com.fredy.askquestions.features.ui.navigation.RootNavGraph
import com.fredy.askquestions.auth.viewModel.AuthEvent
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthViewModel
import com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel.PreferencesViewModel
import com.fredy.theme.DefaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private val viewModel by viewModels<PreferencesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.state.value.updated
            }
        }
//        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val setting by viewModel.state.collectAsStateWithLifecycle()
            val state by authViewModel.state.collectAsStateWithLifecycle()

            val isDarkTheme = when (setting.displayMode) {
                DisplayMode.Light -> false
                DisplayMode.Dark -> true
                DisplayMode.System -> isSystemInDarkTheme()
            }

            if (setting.updated) {
                if (!setting.autoLogin) {
                    authViewModel.onEvent(
                        AuthEvent.SignOut
                    )
                }
                DefaultTheme(darkTheme = isDarkTheme) {
                    val navController = rememberNavController()
                    val startDestination = if (state.signedInUser != null && setting.autoLogin && !setting.bioAuth) Graph.MainNav else Graph.AuthNav
                    RootNavGraph(
                        navController = navController,
                        startDestination = startDestination,
                        preferencesViewModel = viewModel,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

