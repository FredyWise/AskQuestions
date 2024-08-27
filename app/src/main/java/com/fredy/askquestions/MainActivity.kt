package com.fredy.askquestions


import android.app.Activity
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
import com.fredy.askquestions.ui.navigation.Graph
import com.fredy.askquestions.ui.navigation.RootNavGraph
import com.fredy.askquestions.auth.viewModel.AuthEvent
import com.fredy.askquestions.auth.viewModel.AuthViewModel
import com.fredy.core.util.ActivityProvider
import com.fredy.preferences.viewModel.PreferencesViewModel
import com.fredy.theme.DefaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity(), ActivityProvider {
    override fun getActivity(): Activity {
        return this
    }

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

            val isDarkTheme = if(setting.isDarkMode == null) isSystemInDarkTheme() else setting.isDarkMode!!

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

