package com.fredy.askquestions.auth

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.fredy.askquestions.auth.viewModel.AuthEvent
import com.fredy.askquestions.auth.ui.SignIn
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthViewModel
import com.fredy.core.navigation.Graph
import com.fredy.core.navigation.utils.navigateZeroTopTo
import com.fredy.core.resource.Resource


fun NavGraphBuilder.authenticationNavGraph(
    isUsingBioAuth: Boolean,
    viewModel: AuthViewModel,
    rootNavController: NavHostController
) {
    navigation(
        route = Graph.AuthNav,
        startDestination = AuthNavigationRoute.SignIn.route,
    ) {
        composable(
            route = AuthNavigationRoute.SignIn.route,
        ) {
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
                state = state,
                isUsingBioAuth = isUsingBioAuth,
                onEvent = viewModel::onEvent,
            )
        }

    }
}