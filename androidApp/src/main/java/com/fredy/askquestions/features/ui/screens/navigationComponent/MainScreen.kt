package com.fredy.askquestions.features.ui.screens.navigationComponent

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fredy.askquestions.features.domain.models.User
import com.fredy.askquestions.features.ui.navigation.BottomBarNavGraph
import com.fredy.askquestions.features.ui.navigation.NavigationRoute
import com.fredy.askquestions.features.ui.navigation.bottomBarScreens
import com.fredy.askquestions.features.ui.navigation.navigateSingleTopTo
import com.fredy.mysavings.Feature.Presentation.Screens.NavigationComponent.BottomBar
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.surface,
    onContentColor: Color = MaterialTheme.colorScheme.onSurface,
    rootNavController: NavHostController,
    currentUser: User?,
    signOut: () -> Unit,
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = bottomBarScreens.find { it.route == currentDestination?.route } ?: NavigationRoute.SignIn

//    var isShowWarning by remember {
//        mutableStateOf(
//            false
//        )
//    }
//    SimpleWarningDialog(
//        isShowWarning = isShowWarning,
//        onDismissRequest = { isShowWarning = false },
//        onSaveClicked = {
//            signOut()
//        },
//        warningText = "Are You Sure Want to Log Out?"
//    )

    Scaffold(
        modifier = modifier.animateContentSize(),
        containerColor = backgroundColor,
        bottomBar = {
            BottomBar(
                modifier = Modifier.height(
                    65.dp
                ),
                backgroundColor = contentColor,
                contentColor = onContentColor,
                allScreens = bottomBarScreens,
                onTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(
                        newScreen.route
                    )
//                    isShowingAdd = false
                },
                currentScreen = currentScreen
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    innerPadding
                )
        ) {
            Column(
                modifier = modifier.shadow(
                    elevation = 4.dp, clip = true
                )
            ) {
                AppBar(
                    modifier = Modifier,
                    backgroundColor = contentColor,
                    contentColor = onContentColor,
                    onProfilePictureClick = {
                        rootNavController.navigate(
                            NavigationRoute.Profile.route
                        )
                    },
                    currentUser = currentUser
                )
                BottomBarNavGraph(
                    navController = navController,
                    rootNavController = rootNavController,
                )
            }
        }
    }
}