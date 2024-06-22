package com.fredy.askquestions.features.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.fredy.askquestions.features.ui.screens.chatScreen.ChatScreen
import com.fredy.askquestions.features.ui.screens.chatScreen.MessageScreen
import com.fredy.askquestions.features.ui.screens.navigationComponent.MainScreen
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthEvent
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthViewModel
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.ChatViewModel
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageEvent
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageViewModel
import com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel.PreferencesViewModel

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
            val state by viewModel.state.collectAsStateWithLifecycle()
            MessageScreen(
                state = state,
                onEvent = viewModel::onEvent,
            )
        }

    }
}