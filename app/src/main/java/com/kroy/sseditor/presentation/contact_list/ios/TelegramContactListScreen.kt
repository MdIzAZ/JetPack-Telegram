package com.kroy.sseditor.presentation.contact_list.ios

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kroy.sseditor.domain.models.ContactItem
import com.kroy.sseditor.domain.models.dummyContacts
import com.kroy.sseditor.presentation.contact_list.ContactListScreenState
import com.kroy.sseditor.presentation.contact_list.ios.components.BottomNavBar
import com.kroy.sseditor.presentation.contact_list.ios.components.ContactList
import com.kroy.sseditor.presentation.contact_list.ios.components.ContactListScreenTopBar
import com.kroy.sseditor.presentation.contact_list.ios.components.PopUpNotification
import com.kroy.sseditor.presentation.theme.CustomGray
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TelegramContactListScreen(
    state: ContactListScreenState,
    startShowingContacts: () -> Unit,
    onChatClick: (Int) -> Unit,
    onLongPress: () -> Unit,
    onNavigateBack: () -> Unit
) {

    var isAlertDialogOpen by remember { mutableStateOf(false) }
    var showNotification by remember { mutableStateOf(false) }
    var currentNotification by remember { mutableStateOf<ContactItem?>(null) }

    BackHandler {
        isAlertDialogOpen = true
    }


    LaunchedEffect(state.notificationItems.firstOrNull()) {
        state.notificationItems.firstOrNull().let {
            showNotification = false
            delay(300)
            showNotification = true
            currentNotification = it
            delay(3000)
            showNotification = false
            delay(300)
            currentNotification = null
        }
    }

    LaunchedEffect(Unit) {
        startShowingContacts()
    }





    Scaffold(
        topBar = {
            ContactListScreenTopBar(
                time = state.notificationBarTime,
                unreadMessageCount = state.totalUnreadMessages,
                folders = state.folders,
                batteryIcon = state.battery.first,
                batteryPercentage = state.battery.second,
                onLongPress = {}
            )
        },

        bottomBar = {
            BottomNavBar(
                modifier = Modifier
                    .background(color = CustomGray)
                    .fillMaxWidth(),
                count = state.folders.find {
                    it.first.equals("unread", ignoreCase = true)
                }?.second ?: 54,
                onLongPress = onLongPress
            )
        }
    ) { ip ->

        Box(
            modifier = Modifier
                .padding(ip)
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                ContactList(
                    modifier = Modifier.weight(1f),// Fills the remaining space right after the status bar,
                    chats = state.contactItems,
                    onContactClick = onChatClick
                )

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
                                color = colorScheme.secondary,
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


    AnimatedVisibility(
        visible = showNotification,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = 300)
        ),
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        currentNotification?.let { contact ->
            PopUpNotification(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(horizontal = 8.dp),
                contact = contact
            )
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    device = "spec:width=1440px,height=3200px,dpi=800" // This simulates a 6.7-inch screen with 1440x3200 resolution and 560 dpi
)
@Composable
fun TelegramScreenPreview() {

    TelegramContactListScreen(
        ContactListScreenState(contactItems = dummyContacts),
        {},
        {},
        {},
        {})
}
