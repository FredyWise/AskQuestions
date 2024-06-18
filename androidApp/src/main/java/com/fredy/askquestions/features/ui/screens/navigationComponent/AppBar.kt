package com.fredy.askquestions.features.ui.screens.navigationComponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fredy.askquestions.R
import com.fredy.askquestions.features.domain.models.User
import com.fredy.askquestions.features.ui.screens.comonComponents.AsyncImageHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onNavigationIconClick: () -> Unit,
    onProfilePictureClick: () -> Unit,
    currentUser: User?,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor
        ),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer"
                )
            }
        },
        actions = {
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
    )
}


