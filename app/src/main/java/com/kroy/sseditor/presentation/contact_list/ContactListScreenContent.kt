package com.kroy.sseditor.presentation.contact_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.kroy.sseditor.domain.models.OSType
import com.kroy.sseditor.presentation.contact_list.andoid.AndroidContactListScreen
import com.kroy.sseditor.presentation.contact_list.ios.TelegramContactListScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactListScreenContent(
    osType: OSType,
    state: ContactListScreenState,
    startShowingContacts: () -> Unit,
    onChatClick: (Int) -> Unit,
    onLongPress: () -> Unit,
    onNavigateBack: () -> Unit
) {
    
    when(osType) {
        OSType.IOS -> {
            TelegramContactListScreen(
                state = state,
                startShowingContacts = startShowingContacts,
                onChatClick = onChatClick,
                onLongPress = onLongPress,
                onNavigateBack = onNavigateBack
            )
        }
        OSType.Android -> {
            AndroidContactListScreen (
                state = state,
                startShowingContacts = startShowingContacts,
                onChatClick = onChatClick,
                onLongPress = onLongPress,
                onNavigateBack = onNavigateBack
            )
        }
    }
    

}