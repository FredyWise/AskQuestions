package com.fredy.askquestions.features.ui.screens.navigationComponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fredy.askquestions.features.domain.models.User
import com.fredy.askquestions.features.ui.navigation.NavigationRoute
import com.fredy.askquestions.features.ui.navigation.bottomBarScreens
import com.fredy.askquestions.features.ui.navigation.navigateSingleTopTo
import com.fredy.mysavings.Feature.Presentation.Screens.NavigationComponent.BottomBar
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
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
    var isFabVisible by remember {
        mutableStateOf(
            true
        )
    }
    var isShowingAdd by remember {
        mutableStateOf(
            false
        )
    }
    val scope = rememberCoroutineScope()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = bottomBarScreens.find { it.route == currentDestination?.route } ?: NavigationRoute.SignIn

    var isShowWarning by remember {
        mutableStateOf(
            false
        )
    }
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
        floatingActionButton = {
            AnimatedVisibility(visible = isFabVisible) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedVisibility(
                        visible = isShowingAdd,
                        enter = fadeIn(
                            animationSpec = tween(
                                300
                            )
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                300
                            )
                        ),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    rootNavController.navigate(
                                        "${NavigationRoute.Add.route}"
                                    )
                                },
                                containerColor = contentColor,
                                modifier = Modifier.border(
                                    1.dp,
                                    MaterialTheme.colorScheme.secondary.copy(
                                        0.3f
                                    ),
                                    CircleShape
                                ),
                            ) {
                                Icon(
                                    NavigationRoute.Add.icon,
//                    Icons.Default.Add,
                                    modifier = Modifier.size(
                                        30.dp
                                    ),
                                    tint = onContentColor,
                                    contentDescription = ""
                                )
                            }
                            Spacer(
                                modifier = Modifier.height(
                                    8.dp
                                )
                            )
                            FloatingActionButton(
                                onClick = {
                                    rootNavController.navigate(
                                        "${NavigationRoute.BulkAdd.route}"
                                    )
                                },
                                containerColor = contentColor,
                                modifier = Modifier.border(
                                    1.dp,
                                    MaterialTheme.colorScheme.secondary.copy(
                                        0.3f
                                    ),
                                    CircleShape
                                ),
                            ) {
                                Icon(
                                    NavigationRoute.BulkAdd.icon,
                                    modifier = Modifier.size(
                                        30.dp
                                    ),
                                    tint = onContentColor,
                                    contentDescription = ""
                                )
                            }
                            Spacer(
                                modifier = Modifier.height(
                                    8.dp
                                )
                            )
                        }
                    }
                    FloatingActionButton(
                        onClick = {
                            isShowingAdd = !isShowingAdd
                        },
                        containerColor = contentColor,
                        modifier = Modifier.border(
                            1.dp,
                            MaterialTheme.colorScheme.secondary.copy(
                                0.3f
                            ),
                            CircleShape
                        ),
                    ) {
                        Icon(
                            Icons.Default.Add,
                            modifier = Modifier.size(
                                35.dp
                            ),
                            tint = onContentColor,
                            contentDescription = ""
                        )
                    }
                }
            }
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
//                .pointerInput(
//                    Unit
//                ) {
//                    detectVerticalDragGestures()
//                },
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
                    onNavigationIconClick = {},
                    onProfilePictureClick = {
                        rootNavController.navigate(
                            NavigationRoute.Profile.route
                        )
                    },
                    currentUser = currentUser
                )
            }

        }
    }
}