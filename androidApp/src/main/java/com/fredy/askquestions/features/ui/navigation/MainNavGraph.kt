package com.fredy.askquestions.features.ui.navigation

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.fredy.askquestions.features.domain.util.Resource.Resource
import com.fredy.askquestions.features.ui.screens.chatScreen.ChatScreen
import com.fredy.askquestions.features.ui.screens.chatScreen.MessageScreen
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthViewModel
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthEvent
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.ChatViewModel
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageEvent
import com.fredy.askquestions.features.ui.viewmodels.ChatViewModel.MessageViewModel
import com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel.PreferencesViewModel
import com.fredy.mysavings.Feature.Presentation.Screens.Authentication.SignIn
import com.fredy.mysavings.Feature.Presentation.Screens.Authentication.SignUp

fun NavGraphBuilder.mainNavGraph(
    preferencesViewModel: PreferencesViewModel,
    authViewModel: AuthViewModel,
    rootNavController: NavHostController
) {
    navigation(
        route = Graph.MainNav,
        startDestination = NavigationRoute.Chat.route,
    ) {
        composable(
            route = NavigationRoute.Chat.route,
        ) {
            val viewModel: ChatViewModel = hiltViewModel()

            ChatScreen(viewModel = viewModel, navigateToAddChat = {
                rootNavController.navigateSingleTopTo(
                    NavigationRoute.Message.route
                )
            })
        }

        composable(
            route = NavigationRoute.Message.route,
        ) {
            val viewModel: MessageViewModel = hiltViewModel()
//            val state by viewModel.state.collectAsStateWithLifecycle()
            val onEvent = viewModel::onEvent
            MessageScreen(
                state = viewModel.state,
                onTextChange = {
                    onEvent(
                        MessageEvent.OnTextChange(
                            it
                        )
                    )
                },
                onClick = { onEvent(MessageEvent.OnSendClick) },
                messages = viewModel.state.messages
            )
        }

    }
}