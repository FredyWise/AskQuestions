package com.fredy.mysavings.Feature.Presentation.Screens.NavigationComponent

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.fredy.askquestions.features.ui.navigation.NavigationRoute


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    allScreens: List<NavigationRoute>,
    onTabSelected: (NavigationRoute) -> Unit,
    currentScreen: NavigationRoute
) {
    AnimatedPointBottomBarTemplate(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        allScreens = allScreens,
        onTabSelected = onTabSelected,
        currentScreen = currentScreen
    )
}


