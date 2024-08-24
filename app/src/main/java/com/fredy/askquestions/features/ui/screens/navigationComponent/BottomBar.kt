package com.fredy.mysavings.Feature.Presentation.Screens.NavigationComponent

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import com.fredy.askquestions.features.ui.navigation.NavigationRoute
import com.fredy.theme.components.AnimatedPointBottomBarTemplate
import com.fredy.theme.components.BottomBarItemTemplate


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    allScreens: List<NavigationRoute>,
    onTabSelected: (NavigationRoute) -> Unit,
    currentScreen: NavigationRoute
) {
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    AnimatedPointBottomBarTemplate(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        selectedIndex = selectedIndex,
    ){
        allScreens.forEachIndexed { index, screen ->
            BottomBarItemTemplate(
                modifier = Modifier.padding(
                    vertical = 8.dp
                ),
                onClick = {
                    selectedIndex = index
                    onTabSelected(screen)
                },
                selected = currentScreen == screen,
                selectedIcon = screen.icon,
                unselectedIcon = screen.iconNot,
                description = screen.contentDescription,
                selectedColor = contentColor,
                unselectedColor = contentColor.copy(
                    alpha = ContentAlpha.disabled
                ),
                label = screen.title
            )
        }
    }
}


