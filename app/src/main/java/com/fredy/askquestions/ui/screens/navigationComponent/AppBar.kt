package com.fredy.askquestions.ui.screens.navigationComponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.fredy.askquestions.features.ui.screens.comonComponents.Handler.AsyncImageHandler
import com.fredy.core.domain.models.User
import com.fredy.theme.components.AppBarTemplate

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onProfilePictureClick: () -> Unit,
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit,
    currentUser: User?,
) {
    AppBarTemplate(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp
                    )
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        onProfilePictureClick()
                    },
            ) {
                currentUser?.let {
                    AsyncImageHandler(
                        modifier = Modifier.size(
                            40.dp
                        ),
                        imageUrl = it.profilePictureUrl,
                        contentDescription = "Profile picture",
                        imageVector = Icons.Default.AccountCircle,
                    )
                } ?: CircularProgressIndicator(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(
                            36.dp
                        ),
                    strokeWidth = 2.dp,
                    color = contentColor
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Show Search Bar"
                )
            }
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Show Search Bar"
                )
            }
        },
    )
}


