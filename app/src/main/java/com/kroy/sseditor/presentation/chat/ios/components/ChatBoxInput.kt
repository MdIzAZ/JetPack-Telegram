package com.kroy.sseditor.presentation.chat.ios.components

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import com.kroy.sseditor.domain.models.MessageType
import com.kroy.sseditor.presentation.theme.BluishGray
import com.kroy.sseditor.presentation.theme.BottomIconTint
import com.kroy.sseditor.presentation.theme.CustomGray
import com.kroy.sseditor.presentation.theme.CustomPurple

@Preview(showSystemUi = true)
@Composable
fun ChatBoxInput(
    txtFieldValue: String = "",
    sendMessage: (msg: MessageType) -> Unit = {},
    onClipBtnClick: () -> Unit = {},
    onTxtFieldValueChange: (String) -> Unit = {},
    onStickerClick:()->Unit = {}
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current
    val isKeyboardOpen = WindowInsets.ime.getBottom(density) > 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = CustomGray)
            .graphicsLayer {
                shape = RoundedCornerShape(0.dp)
                clip = true
            }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Attachment file icon at the start


            IconButton(
                onClick = {
                    onClipBtnClick()
                },
                content = {
                    Icon(
                        painter = painterResource(id = com.kroy.ssediotor.R.drawable.attach),
                        contentDescription = "Attach",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(32.dp),
                        tint = BottomIconTint
                    )
                }

            )


            StickerCompatibleInput(
                modifier = Modifier.weight(1f),
                text = txtFieldValue,
                onImageReceived = {
                    sendMessage(MessageType.Sticker(it))
                },
                onTextChanged = { onTxtFieldValueChange(it) },
                onSendMessage = {
                    sendMessage(MessageType.Text(txtFieldValue))
                },
                onStickerClick = onStickerClick
            )



            if (txtFieldValue.isBlank()) {
                Icon(
                    painter = painterResource(id = com.kroy.ssediotor.R.drawable.ic_mic),
                    contentDescription = "Mic",
                    tint = BottomIconTint,
                    modifier = Modifier
                        .padding(end = 10.dp, start = 10.dp)
                        .height(50.dp)
                )
            } else {
                Icon(
                    painterResource(id = com.kroy.ssediotor.R.drawable.ic_up_arrow),
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier
                        .background(color = CustomPurple, shape = CircleShape)
                        .padding(8.dp)
                        .size(20.dp)
                        .clickable {
                            sendMessage(MessageType.Text(txtFieldValue))
                        }
                )
            }


        }

        if (!isKeyboardOpen) {
            Spacer(Modifier.height(24.dp))
        }

//        if (!isKeyboardOpen) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .fillMaxWidth(.4f)
//                    .padding(top = 20.dp)
//                    .height(5.dp)
//                    .background(Color.White)
//            )
//        }
    }


}


@Composable
fun StickerCompatibleInput(
    modifier: Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onImageReceived: (Uri) -> Unit,
    onSendMessage: () -> Unit,
    onStickerClick: () -> Unit  // Added callback for sticker icon
) {

    val context = LocalContext.current
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


    var editTextRef by remember { mutableStateOf<EditText?>(null) }
    var emojiIconRef by remember { mutableStateOf<ImageView?>(null) }
    var stickerIconRef by remember { mutableStateOf<ImageView?>(null) }

    AndroidView(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(Color.Black),
        factory = {
            val container = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(Color.Black.toArgb())
                gravity = Gravity.CENTER_VERTICAL
                setPadding(12, 6, 4, 6)
            }

            val editText = EditText(context).apply {
                hint = "Message"
                setText(text)
                setBackgroundColor(Color.Black.toArgb())
                setTextColor(Color.White.toArgb())
                setHintTextColor(android.graphics.Color.GRAY)
                textSize = 16f
                imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_SEND
                setPadding(12, 6, 6, 12)

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                        if (text.isNotBlank()) {
                            onSendMessage()
                            imm.hideSoftInputFromWindow(windowToken, 0)
                            true
                        } else false
                    } else false
                }

                addTextChangedListener {
                    onTextChanged(it?.toString() ?: "")
                }

                ViewCompat.setOnReceiveContentListener(this, arrayOf("image/*")) { _, payload ->
                    val uri = payload.clip.getItemAt(0).uri
                    onImageReceived(uri)
                    null
                }

                // Ensure EditText takes remaining space
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            // Trailing sticker icon
            val stickerIcon = ImageView(context).apply {
                setImageResource(com.kroy.ssediotor.R.drawable.ic_sticker)
                imageTintList = ColorStateList.valueOf(BottomIconTint.toArgb())
                setPadding(0, 0, 16, 0)
                layoutParams = LinearLayout.LayoutParams(
                    24.dpToPx(context),
                    24.dpToPx(context)
                )
                setOnClickListener { onStickerClick() }
            }

            val emojiIcon = ImageView(context).apply {
                setImageResource(com.kroy.ssediotor.R.drawable.ic_happy_face)
                imageTintList = ColorStateList.valueOf(BottomIconTint.toArgb())
                setPadding(0, 0, 16, 0)
                layoutParams = LinearLayout.LayoutParams(
                    24.dpToPx(context),
                    24.dpToPx(context)
                )
                setOnClickListener {  }
            }


            editTextRef = editText
            stickerIconRef = stickerIcon
            emojiIconRef = emojiIcon


            container.addView(editText)
            container.addView(stickerIcon)
            container
        },
        update = { container ->
            val editText = editTextRef ?: return@AndroidView

            // Sync text field state
            if (editText.text.toString() != text) {
                editText.setText(text)
                editText.setSelection(text.length)
            }

            // Remove trailing icon if it exists
            while (container.childCount > 1) {
                container.removeViewAt(container.childCount - 1)
            }

            // Detach the icon from any existing parent before re-adding
            val iconToAdd = if (text.isBlank()) stickerIconRef else emojiIconRef
            iconToAdd?.parent?.let { (it as? ViewGroup)?.removeView(iconToAdd) }

            iconToAdd?.let { container.addView(it) }
        }
    )
}

// Utility function to convert dp to pixels
private fun Int.dpToPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}


