package com.kroy.sseditor.presentation.contact_list.andoid

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kroy.sseditor.presentation.common_components.vibrate
import com.kroy.sseditor.presentation.contact_list.ContactListScreenState
import com.kroy.sseditor.presentation.contact_list.andoid.components.AndroidContactScreenCompleteTop
import com.kroy.sseditor.presentation.contact_list.andoid.components.TelegramContactRowAndroid
import com.kroy.sseditor.presentation.theme.DARK_GRAY
import com.kroy.sseditor.presentation.theme.DarkBluishGray
import com.kroy.sseditor.presentation.theme.TelegramAndroidUnseenMessage
import com.kroy.sseditor.presentation.theme.TelegramBlack
import com.kroy.sseditor.presentation.theme.TelegramBlueColor
import com.kroy.sseditor.presentation.theme.TelegramLight

@Composable
fun AndroidContactListScreen(
    modifier: Modifier = Modifier,
    state: ContactListScreenState,
    startShowingContacts: () -> Unit,
    onChatClick: (Int) -> Unit,
    onLongPress: () -> Unit,
    onNavigateBack: () -> Unit
) {

    var isAlertDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler {
        isAlertDialogOpen = true
    }

    LaunchedEffect(Unit) {
        startShowingContacts()
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            AndroidContactScreenCompleteTop(
                time = state.notificationBarTime,
                unreadMessageCount = state.totalUnreadMessages,
                folder = state.folders,
                battery = state.battery
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = CircleShape,
                containerColor = TelegramBlueColor,
                modifier = Modifier
                    .padding(16.dp)

            ) {
                Icon(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    vibrate(context)
                                    onLongPress()
                                }
                            )
                        },
                    tint = Color.White,
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Add Contact"
                )
            }
        }
    ) { ip ->

        Box(
            Modifier
                .background(TelegramBlack)
                .fillMaxSize()
                .padding(ip)
        ) {

            LazyColumn(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = modifier.fillMaxSize()
            ) {
                itemsIndexed(state.contactItems) { index, contact ->
                    TelegramContactRowAndroid(
                        contact = contact,
                        time = contact.uiTime,
                        onContactClick = { onChatClick(contact.id) }
                    )
                }
            }


            if (isAlertDialogOpen) {
                AlertDialog(
                    modifier = Modifier.align(Alignment.Center),
                    title = {
                        Text(
                            text = "Confirm Exit",
                            color = colorScheme.onSurface,
                            style = typography.headlineSmall
                        )
                    },
                    text = {
                        Text(
                            text = "Are you sure you want to go back? All data on the Contact List and Chat List screens will be lost.",
                            color = colorScheme.onSurfaceVariant,
                            style = typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { isAlertDialogOpen = false }) {
                            Text(
                                text = "Cancel",
                                color = colorScheme.error,
                                style = typography.labelLarge
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                onNavigateBack()
                                isAlertDialogOpen = false
                            }
                        ) {
                            Text(
                                text = "OK",
                                color = colorScheme.primary,
                                style = typography.labelLarge
                            )
                        }
                    },
                    onDismissRequest = { isAlertDialogOpen = false },
                    tonalElevation = 6.dp

                )
            }
        }


    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    device = "spec:width=1440px,height=3200px,dpi=800" // This simulates a 6.7-inch screen with 1440x3200 resolution and 560 dpi
)
@Composable
fun TelegramAndroidContactListScreenPreview() {

    AndroidContactListScreen(
        Modifier,
        ContactListScreenState(contactItems = emptyList()),
        {},
        {},
        {},
        {})
}