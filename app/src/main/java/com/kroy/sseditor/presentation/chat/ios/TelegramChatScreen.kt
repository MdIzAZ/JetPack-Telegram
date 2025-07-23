package com.kroy.sseditor.presentation.chat.ios

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kroy.ssediotor.R
import com.kroy.sseditor.domain.models.MessageType
import com.kroy.sseditor.domain.models.dummyChatMessages
import com.kroy.sseditor.presentation.chat.ChatScreenState
import com.kroy.sseditor.presentation.chat.ios.components.ChatBoxInput
import com.kroy.sseditor.presentation.chat.ios.components.ChatListSection
import com.kroy.sseditor.presentation.chat.ios.components.ChatScreenTopBar
import com.kroy.sseditor.utils.Utils


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TelegramChatScreen(
    contactId: Int,
    state: ChatScreenState,
    onBackClick: () -> Unit,
    onMessageSend: (contactId: Int, msg: MessageType) -> Unit,
    editMessage: (contactId: Int, messageId: Int, msg: String) -> Unit
) {


    val context = LocalContext.current

    var imageOrSticker by remember { mutableStateOf(0) }  // 0-> Img, 1-> Sticker

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                when (imageOrSticker) {
                    0 -> onMessageSend(contactId, MessageType.Image(it))
                    1 -> onMessageSend(contactId, MessageType.Sticker(it))
                }
            }
        }
    )


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                imagePickerLauncher.launch(PickVisualMediaRequest())
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    var isEditing by remember { mutableStateOf(false) }
    var txtFieldValue by remember { mutableStateOf("") }
    var editingMsgId by remember { mutableStateOf(-4) }


    LaunchedEffect(editingMsgId) {
        if (editingMsgId >= 0) {
            txtFieldValue = state.messages.find { it.id == editingMsgId }?.text.orEmpty()
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            ChatScreenTopBar(
                time = state.notificationBarTime,
                contactName = state.contactName,
                contactPic = state.contactPic,
                numberOfUnseenMessages = state.numberOfUnseenMessages,
                batteryPercentage = state.battery.second,
                batteryIcon = state.battery.first,
                onBackClick = onBackClick,
                color = state.backgroundColor
            )
        },
        bottomBar = {
            ChatBoxInput(
                txtFieldValue = txtFieldValue,
                onClipBtnClick = {
                    imageOrSticker = 0
                    if (!hasExternalStoragePermission(context)) {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        imagePickerLauncher.launch(PickVisualMediaRequest())
                    }
                },
                onTxtFieldValueChange = {
                    txtFieldValue = it
                },
                sendMessage = {
                    if (!isEditing) {
                        onMessageSend(contactId, it)
                        txtFieldValue = ""
                    } else {
                        editMessage(contactId, editingMsgId, txtFieldValue)
                        txtFieldValue = ""
                        isEditing = false
                        editingMsgId = -4
                    }
                },
                onStickerClick = {
                    imageOrSticker = 1
                    if (!hasExternalStoragePermission(context)) {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        imagePickerLauncher.launch(PickVisualMediaRequest())
                    }
                }
            )
        }
    ) { ip ->
        Box(
            modifier = Modifier
//                .padding(ip)
                .fillMaxSize()
                .background(Color(0xFF1B1E2E))
        ) {


            if (state.backgroundImage != null) {
                Image(
                    bitmap = state.backgroundImage.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .blur(20.dp, 20.dp)
                        .fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(ip)
            ) {

                ChatListSection(
                    modifier = Modifier.weight(1f),
                    chats = state.messages,
                    lastReceiverMsgTime = state.lastMessageTime,
                    onLongPress = { id ->
                        isEditing = true
                        editingMsgId = id
                    }
                )

            }
        }
    }
}


fun hasExternalStoragePermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // API 33+: Check READ_MEDIA_IMAGES for file access (optional for PickVisualMedia)
        context.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
    } else {
        // API 32 and below: Check READ_EXTERNAL_STORAGE
        context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    device = "spec:width=1440px,height=3200px,dpi=560"
)
@Composable
fun PreviewTelegram() {
    val context = LocalContext.current
    TelegramChatScreen(
        contactId = 404,
        state = ChatScreenState(
            contactName = "Rakesh",
            messages = dummyChatMessages,
            backgroundImage = Utils.getBitmapFromResource(context, R.drawable.d)
        ),
        onBackClick = {},
        onMessageSend = { _, _ -> },
        editMessage = { _, _, _ -> }
    )
}


