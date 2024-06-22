package com.fredy.askquestions.features.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthViewModel
import com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel.PreferencesViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    startDestination: String,
    preferencesViewModel: PreferencesViewModel,
    authViewModel: AuthViewModel,
) {
    NavHost(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background
        ),
        navController = navController,
        route = Graph.RootNav,
        startDestination = startDestination,
    ) {
        authenticationNavGraph(
            preferencesViewModel,
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

