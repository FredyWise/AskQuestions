package com.fredy.askquestions.features.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.fredy.askquestions.features.domain.util.Resource.Resource
import com.fredy.askquestions.features.ui.util.navigateZeroTopTo
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthEvent
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthViewModel
import com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel.PreferencesViewModel
import com.fredy.mysavings.Feature.Presentation.Screens.Authentication.SignIn

fun NavGraphBuilder.authenticationNavGraph(
    preferencesViewModel: PreferencesViewModel,
    viewModel: AuthViewModel,
    rootNavController: NavHostController
) {
    navigation(
        route = Graph.AuthNav,
        startDestination = NavigationRoute.SignIn.route,
    ) {
        composable(
            route = NavigationRoute.SignIn.route,
        ) {
            val setting by preferencesViewModel.state.collectAsStateWithLifecycle()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val context = LocalContext.current
            LaunchedEffect(
                key1 = state.bioAuthResource,
            ) {
                when (state.bioAuthResource) {
                    is Resource.Error -> {
                        val error = " state.bioAuthResource.error.name"
                        Toast.makeText(
                            context,
                            "${error}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Success -> {
                        Toast.makeText(
                            context,
                            "state.bioAuthResource.data",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.onEvent(
                            AuthEvent.GetCurrentUser
                        )
                        rootNavController.navigateZeroTopTo(
                            Graph.MainNav
                        )
                    }

                    else -> {
                    }
                }
            }
            LaunchedEffect(
                key1 = state.authResource,
            ) {
                when (state.authResource) {
                    is Resource.Error -> {
                        viewModel.onEvent(
                            AuthEvent.SignOut
                        )
                        val error = "state.authResource.message"
                        Toast.makeText(
                            context,
                            "${error}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Success -> {
                        Toast.makeText(
                            context,
                            "SignIn Success",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.onEvent(
                            AuthEvent.GetCurrentUser
                        )
                        rootNavController.navigateZeroTopTo(
                            Graph.MainNav
                        )
                    }

                    else -> {
                    }
                }
            }

            SignIn(
                navController = rootNavController,
                state = state,
                isUsingBioAuth = setting.bioAuth,
                onEvent = viewModel::onEvent,
            )
        }

    }
}