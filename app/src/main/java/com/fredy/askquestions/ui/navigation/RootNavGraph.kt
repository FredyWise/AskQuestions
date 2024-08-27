package com.fredy.askquestions.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.fredy.askquestions.auth.authenticationNavGraph
import com.fredy.askquestions.auth.viewModel.AuthViewModel
import com.fredy.preferences.viewModel.PreferencesViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String,
    preferencesViewModel: PreferencesViewModel,
    authViewModel: AuthViewModel,
) {
    NavHost(
        modifier = modifier.background(
            MaterialTheme.colorScheme.background
        ),
        navController = navController,
        route = Graph.RootNav,
        startDestination = startDestination,
    ) {
        authenticationNavGraph(
            preferencesViewModel.state.value.bioAuth,
            authViewModel,
            navController
        )
        mainNavGraph(
            preferencesViewModel,
            authViewModel,
            navController
        )

    }
}

