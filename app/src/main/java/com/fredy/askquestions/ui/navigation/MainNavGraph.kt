package com.fredy.askquestions.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.fredy.askquestions.ui.screens.navigationComponent.MainScreen
import com.fredy.askquestions.auth.viewModel.AuthEvent
import com.fredy.askquestions.auth.viewModel.AuthViewModel
import com.fredy.chat.ui.MessageScreen
import com.fredy.chat.viewmodels.MessageViewModel
import com.fredy.preferences.viewModel.PreferencesViewModel

fun NavGraphBuilder.mainNavGraph(
    preferencesViewModel: PreferencesViewModel,
    authViewModel: AuthViewModel,
    rootNavController: NavHostController
) {
    navigation(
        route = Graph.MainNav,
        startDestination = Graph.BottomBarNav,
    ) {
        composable(
            route = Graph.BottomBarNav,
        ) {
            val state by authViewModel.state.collectAsStateWithLifecycle()
            MainScreen(
                rootNavController = rootNavController,
                currentUser = state.signedInUser,
                navigateToMessageScreen = {
                    rootNavController.navigate(
                        "${NavigationRoute.Message.route}?chatId=$it"
                    )
                },
                signOut = {
                    authViewModel.onEvent(
                        AuthEvent.SignOut
                    )
                }
            )
        }

        composable(
            route = "${NavigationRoute.Message.route}?chatId={chatId}",
            arguments = listOf(
                navArgument(
                    name = "chatId"
                ) {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                },
            )
        ) {
            val viewModel: MessageViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val messages = viewModel.message.collectAsLazyPagingItems()
            MessageScreen(
                state = state,
                onEvent = viewModel::onEvent,
                messages = messages
            )
        }

    }
}