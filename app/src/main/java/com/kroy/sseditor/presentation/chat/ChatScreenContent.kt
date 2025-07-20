package com.kroy.sseditor.presentation.chat

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.kroy.sseditor.domain.models.MessageType
import com.kroy.sseditor.domain.models.OSType
import com.kroy.sseditor.presentation.chat.android.AndroidChatScreen
import com.kroy.sseditor.presentation.chat.ios.TelegramChatScreen

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreenContent(
    contactId: Int,/* Although passing contact id inside state , but still pass directly other wise not able to get when sending msg*/
    osType: OSType,
    state: ChatScreenState,
    onBackClick: () -> Unit,
    onMessageSend: (Int, MessageType) -> Unit,
    editMessage: (contactId: Int, messageId: Int, msg: String) -> Unit
) {

    when (osType) {
        OSType.IOS -> {
            TelegramChatScreen(
                contactId = contactId,/* Although passing contact id inside state , but still pass directly other wise not able to get when sending msg*/
                state = state,
                onBackClick = onBackClick,
                onMessageSend = onMessageSend,
                editMessage = editMessage
            )
        }

        OSType.Android -> {
            AndroidChatScreen(
                contactId=contactId,/* Although passing contact id inside state , but still pass directly other wise not able to get when sending msg*/
                state = state,
                onBackClick = onBackClick,
                onMessageSend = onMessageSend,
                editMessage = editMessage
            )
        }
    }


}