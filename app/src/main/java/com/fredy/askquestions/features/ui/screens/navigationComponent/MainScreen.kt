package com.fredy.askquestions.features.ui.screens.navigationComponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fredy.askquestions.features.domain.models.User
import com.fredy.askquestions.features.ui.navigation.BottomBarNavGraph
import com.fredy.askquestions.features.ui.navigation.NavigationRoute
import com.fredy.askquestions.features.ui.navigation.bottomBarScreens
import com.fredy.askquestions.features.ui.screens.comonComponents.SimpleWarningDialog
import com.fredy.askquestions.features.ui.util.navigateSingleTopTo
import com.fredy.mysavings.Feature.Presentation.Screens.NavigationComponent.BottomBar


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    rootNavController: NavHostController,
    currentUser: User?,
    navigateToMessageScreen: (String?) -> Unit,
    signOut: () -> Unit,
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = bottomBarScreens.find { it.route == currentDestination?.route } ?: NavigationRoute.SignIn

    var isFabVisible by remember {
        mutableStateOf(
            true
        )
    }
    var isShowWarning by remember {
        mutableStateOf(
            false
        )
    }
    SimpleWarningDialog(
        isShowWarning = isShowWarning,
        onDismissRequest = { isShowWarning = false },
        onSaveClicked = {
            signOut()
        },
        warningText = "Are You Sure Want to Log Out?"
    )
    Scaffold(
        modifier = modifier
            .animateContentSize()
            .padding(
                horizontal = 4.dp
            ),
        containerColor = backgroundColor,
        topBar = {
            AppBar(
                modifier = Modifier.shadow(
                    elevation = 4.dp, clip = true
                ),
                backgroundColor = containerColor,
                contentColor = contentColor,
                onProfilePictureClick = {
                    rootNavController.navigate(
                        NavigationRoute.Profile.route
                    )
                },
                onSearchClick = {

                },
                onMoreClick = {

                },
                currentUser = currentUser,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = isFabVisible) {
                FloatingActionButton(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = contentColor.copy(
                            0.3f
                        ),
                        shape = MaterialTheme.shapes.medium,
                    ),
                    contentColor = contentColor,
                    containerColor = containerColor,
                    onClick = {
                        navigateToMessageScreen(
                            null
                        )
                    },
                ) {
                    Icon(
                        Icons.Default.ChatBubble,
                        contentDescription = "Add Chat",
                        tint = contentColor,
                    )
                }
            }
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.height(
                    65.dp
                ),
                backgroundColor = containerColor,
                contentColor = contentColor,
                allScreens = bottomBarScreens,
                onTabSelected = { newScreen ->
                    isFabVisible = newScreen.route == NavigationRoute.Chat.route
                    navController.navigateSingleTopTo(
                        newScreen.route
                    )
                },
                currentScreen = currentScreen
            )
        },
    ) { innerPadding ->
        BottomBarNavGraph(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            rootNavController = rootNavController,
        )
    }
}