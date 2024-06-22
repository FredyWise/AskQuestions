package com.fredy.askquestions.features.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DataSaverOff
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LineAxis
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.AddToPhotos
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.DataSaverOff
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.LineAxis
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.ui.graphics.vector.ImageVector

object Graph {
    const val RootNav = "root_nav"
    const val AuthNav = "auth_nav"
    const val BottomBarNav = "bottom_bar_nav"
    const val MainNav = "main_nav"
}


val drawerScreens = listOf(
    NavigationRoute.Preferences,
)

val bottomBarScreens = listOf(
    NavigationRoute.Chat,
    NavigationRoute.Contacts,
)

sealed class NavigationRoute(
    val route: String,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector,
    val iconNot: ImageVector,
) {
    //auth
    object SignIn: NavigationRoute(
        route = "signIn",
        title = "Sign In",
        contentDescription = "Go Sign In",
        icon = Icons.Default.Login,
        iconNot = Icons.Outlined.Login
    )

//    object SignUp: NavigationRoute(
//        route = "signUp",
//        title = "Sign Up",
//        contentDescription = "Go Sign Up",
//        icon = Icons.Default.HowToReg,
//        iconNot = Icons.Outlined.HowToReg
//    )


    //top bar

    object Profile: NavigationRoute(
        route = "profile",
        title = "Profile",
        contentDescription = "Go to profile screen",
        icon = Icons.Default.Settings,
        iconNot = Icons.Outlined.Settings
    )

    object Preferences: NavigationRoute(
        route = "preferences",
        title = "Preferences",
        contentDescription = "Go to preferences screen",
        icon = Icons.Default.Settings,
        iconNot = Icons.Outlined.Settings
    )



    //bottom bar
    object Chat: NavigationRoute(
        route = "chat",
        title = "Chat",
        contentDescription = "Go to Chat Screen",
        icon = Icons.Default.Receipt,
        iconNot = Icons.Outlined.Receipt
    )

    object Contacts: NavigationRoute(
        route = "contacts",
        title = "Contacts",
        contentDescription = "Go to Contacts Screen",
        icon = Icons.Default.People,
        iconNot = Icons.Outlined.People
    )

    //other screen
    object Message: NavigationRoute(
        route = "message",
        title = "Message",
        contentDescription = "Go to Message Screen",
        icon = Icons.Default.Label,
        iconNot = Icons.Outlined.Label
    )
    object Search: NavigationRoute(
        route = "search",
        title = "Search",
        contentDescription = "Go to Search Screen",
        icon = Icons.Default.Search,
        iconNot = Icons.Outlined.Search
    )

}
//Icons.Default.CenterFocusStrong