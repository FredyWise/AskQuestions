package com.fredy.askquestions.features.ui.screens.comonComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SimpleWarningDialog(
    isShowWarning: Boolean = false,
    onDismissRequest: () -> Unit,
    onSaveClicked: () -> Unit,
    warningText: String
) {
    if (isShowWarning) {
        SimpleDialog(
            title = "Warning!!",
            cancelName = "No",
            saveName = "Yes",
            onDismissRequest = {
                onDismissRequest()
            },
            onSaveClicked = {
                onSaveClicked()
                onDismissRequest()
            },
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = warningText,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SimpleAlertDialog(
    modifier: Modifier = Modifier,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    title: String,
    onDismissRequest: () -> Unit,
    leftButton: @Composable (() -> Unit)? = null,
    rightButton: @Composable () -> Unit = { },
    content: @Composable () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    8.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        },
        onDismissRequest = onDismissRequest,
        dismissButton = leftButton,
        confirmButton = rightButton,
    )
}

@Composable
fun SimpleDialog(
    modifier: Modifier = Modifier,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    dismissOnSave: Boolean = true,
    title: String,
    cancelName: String = "Cancel",
    saveName: String = "Save",
    onDismissRequest: () -> Unit,
    onSaveClicked: () -> Unit,
    additionalExitButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        title = {
            Row{
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    textAlign = TextAlign.Center
                )
                additionalExitButton()
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    4.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    modifier = Modifier
                        .weight(0.4f)
                        .clip(
                            MaterialTheme.shapes.small
                        ),
                    onClick = onDismissRequest,
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        style = MaterialTheme.typography.titleMedium,
                        text = cancelName
                    )
                }
                Spacer(
                    modifier = Modifier.weight(
                        0.05f
                    )
                )
                Button(
                    modifier = Modifier
                        .weight(0.4f)
                        .clip(
                            MaterialTheme.shapes.small
                        ),
                    onClick = {
                        onSaveClicked()
                        if (dismissOnSave) {
                            onDismissRequest()
                        }
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        style = MaterialTheme.typography.titleMedium,
                        text = saveName
                    )
                }
            }
        },
    )
}