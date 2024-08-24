package com.fredy.askquestions.features.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fredy.askquestions.features.ui.screens.chatScreen.ChatScreen
import com.fredy.askquestions.features.ui.screens.contactScreen.ContactScreen
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.ChatViewModel

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
                    rootNavController.navigate(
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
            ContactScreen(
//                state = state,
//                onEvent = viewModel::onEvent,
            )
        }

    }
}