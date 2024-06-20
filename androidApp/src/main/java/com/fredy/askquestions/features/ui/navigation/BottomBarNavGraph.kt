package com.fredy.askquestions.features.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
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

@Composable
fun BottomBarNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Chat.route,
        modifier = modifier,
    ) {
        composable(
            route = NavigationRoute.Chat.route,
        ) {
            val viewModel: ChatViewModel = hiltViewModel()

            ChatScreen(
                viewModel = viewModel,
                navigateToMessageScreen = {
                    rootNavController.navigateSingleTopTo(
                        "${NavigationRoute.Message.route}?chatId=$it"
                    )
                },
            )
        }

        composable(
            route = NavigationRoute.Contacts.route,
        ) {
//            val viewModel: ContactsViewModel = hiltViewModel()
//            val state by viewModel.state.collectAsStateWithLifecycle()
//            ContactScreen(
//                state = state,
//                onEvent = viewModel::onEvent,
//            )
        }

    }
}