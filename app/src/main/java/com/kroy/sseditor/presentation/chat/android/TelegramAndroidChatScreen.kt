package com.kroy.sseditor.presentation.chat.android

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.createBitmap
import com.kroy.ssediotor.R
import com.kroy.sseditor.domain.models.MessageType
import com.kroy.sseditor.presentation.chat.ChatScreenState
import com.kroy.sseditor.presentation.chat.android.components.AndroidChatBottomBar
import com.kroy.sseditor.presentation.chat.android.components.AndroidChatListSection
import com.kroy.sseditor.presentation.chat.android.components.TelegramAndroidChatCompleteTop
import com.kroy.sseditor.presentation.chat.ios.hasExternalStoragePermission
import com.kroy.sseditor.presentation.theme.DarkBluishGray
import com.kroy.sseditor.utils.Utils

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AndroidChatScreen(
    contactId: Int,
    state: ChatScreenState,
    onBackClick: () -> Unit,
    onMessageSend: (contactId: Int, MessageType) -> Unit,
    editMessage: (contactId: Int, messageId: Int, msg: String) -> Unit
) {

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                onMessageSend(contactId, MessageType.Image(it))
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
        modifier = Modifier.imePadding(),
        topBar = {
            TelegramAndroidChatCompleteTop(
                time = state.notificationBarTime,
                name = state.contactName,
                profilePic = state.contactPic,
                battery = state.battery,
                color = state.backgroundColor,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            AndroidChatBottomBar(
                txtFieldValue = txtFieldValue,
                onClipIconClick = {
                    if (!hasExternalStoragePermission(context)) {
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        imagePickerLauncher.launch(PickVisualMediaRequest())
                    }
                },
                onTxtFieldValueChange = {
                    txtFieldValue = it
                },
                onSendClick = {
                    if (!isEditing) {
                        onMessageSend(contactId, MessageType.Text(txtFieldValue))
                        txtFieldValue = ""
                    } else {
                        editMessage(contactId, editingMsgId, txtFieldValue)
                        txtFieldValue = ""
                        isEditing = false
                        editingMsgId = -4

                    }
                },
                onImageReceived = {
                    onMessageSend(contactId, MessageType.Sticker(it))
                }
            )
        }

    ) { ip ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(ip)
        ) {

            val bitmap = remember {
                getBitmapFromVectorDrawable(context, R.drawable.telegram_chat_background)
            }
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = DarkBluishGray)
                )
            }
            AndroidChatListSection(
                modifier = Modifier
                    .fillMaxSize(),
                chats = state.messages,
                onLongPress = { id ->
                    isEditing = true
                    editingMsgId = id
                },
                lastReceiverMsgTime = state.lastMessageTime
            )
        }


    }
}


@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    device = "spec:width=1440px,height=3200px,dpi=560"
)
@Composable
fun PreviewTelegram() {

    val context = LocalContext.current

    AndroidChatScreen(
        contactId = 404,
        state = ChatScreenState(
            messages = emptyList(),
            backgroundImage = Utils.getBitmapFromResource(context, R.drawable.d)
        ),
        onBackClick = {},
        onMessageSend = {_,_->},
        editMessage = { _, _, _ -> }
    )
}


fun getBitmapFromVectorDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap? {
    val drawable = AppCompatResources.getDrawable(context, drawableId) ?: return null

    val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 512
    val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 512

    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}