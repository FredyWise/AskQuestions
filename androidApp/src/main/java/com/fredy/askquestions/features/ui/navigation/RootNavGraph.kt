package com.fredy.askquestions.features.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthViewModel
import com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel.PreferencesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
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
            rootNavController = navController
        )
        navigation(
            route = Graph.MainNav,
            startDestination = Graph.HomeNav,
        ) {
            composable(Graph.HomeNav){

            }

        }

    }
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(
    route
) {
    popUpTo(
        this@navigateSingleTopTo.graph.findStartDestination().id
    ) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true

}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(
            navGraphRoute
        )
    }
    return hiltViewModel(parentEntry)
}
