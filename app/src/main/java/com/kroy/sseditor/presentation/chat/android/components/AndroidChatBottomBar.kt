package com.kroy.sseditor.presentation.chat.android.components

import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.kroy.ssediotor.R
import com.kroy.sseditor.domain.models.MessageType
import com.kroy.sseditor.presentation.theme.BluishGray
import com.kroy.sseditor.presentation.theme.TelegramBlueColor
import com.kroy.sseditor.presentation.theme.TelegramLight


/*

@Preview
@Composable
fun AndroidChatBottomBar(
    modifier: Modifier = Modifier,
    txtFieldValue: String = "",
    onClipIconClick: () -> Unit = {},
    onTxtFieldValueChange: (String) -> Unit = {},
    onSendClick: (MessageType) -> Unit = {}
) {

    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(txtFieldValue) {
        if (txtFieldValue.isNotBlank()) {
            keyboardController?.show()
        }
    }

    TextField(
        modifier = modifier
            .heightIn(50.dp, 60.dp)
            .fillMaxWidth()
            .background(BluishGray),
        value = txtFieldValue,
        onValueChange = onTxtFieldValueChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BluishGray,
            unfocusedContainerColor = BluishGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = { Text("Message") },
        textStyle = TextStyle(
            color = Color.White
        ),

        leadingIcon = {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Outlined.EmojiEmotions,
                tint = Color.Gray,
                contentDescription = null,
            )
        },
        trailingIcon = {
            TrailingIconContent(
                isEmpty = txtFieldValue.isEmpty(),
                onClipIconClick = onClipIconClick,
                onSendClick = {
                    keyboardController?.hide()
                    onSendClick(MessageType.Text(txtFieldValue))
                }
            )
        }

    )


}

*/


@Composable
fun TrailingIconContent(
    isEmpty: Boolean,
    onClipIconClick: () -> Unit,
    onSendClick: () -> Unit
) {
    if (isEmpty) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        scaleY = -1f
                        scaleX = -1f
                    }
                    .clickable { onClipIconClick() },
                painter = painterResource(R.drawable.ic_attach_file),
                tint = Color.Gray,
                contentDescription = null,
            )

            Spacer(Modifier.width(16.dp))

            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_mic),
                tint = Color.Gray,
                contentDescription = null,
            )

            Spacer(Modifier.width(8.dp))

        }
    } else {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable { onSendClick() },
            imageVector = Icons.AutoMirrored.Filled.Send,
            tint = TelegramLight,
            contentDescription = null,
        )
    }

}


@Preview
@Composable
fun AndroidChatBottomBar(
    modifier: Modifier = Modifier,
    txtFieldValue: String ="",
    onTxtFieldValueChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
    onImageReceived: (Uri) -> Unit = {},
    onClipIconClick: () -> Unit = {},
) {

    val context = LocalContext.current
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


    var editTextRef by remember { mutableStateOf<EditText?>(null) }
    var clipIconRef by remember { mutableStateOf<ImageView?>(null) }
    var micIconRef by remember { mutableStateOf<ImageView?>(null) }
    var sendIconRef by remember { mutableStateOf<ImageView?>(null) }

    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { context ->

            val container = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)
                setBackgroundColor(BluishGray.toArgb())
                gravity = Gravity.CENTER_VERTICAL
            }

            // Emoji icon
            val emojiIcon = ImageView(context).apply {
                setImageResource(R.drawable.ic_happy_face)
                setColorFilter(Color.Gray.toArgb())
                setPadding(16, 0, 16, 0)
                layoutParams = ViewGroup.LayoutParams(
                    36.dpToPx(context), 36.dpToPx(context)
                )
                setOnClickListener {  }
            }

            // Create EditText
            val editText = EditText(context).apply {
                setText(txtFieldValue)
                hint = "Message"
                layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
                maxLines = 4
                isSingleLine = false
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                imeOptions = EditorInfo.IME_ACTION_SEND

                setPadding(16, 8,4,8)
                setTextColor(Color.White.toArgb())
                setHintTextColor(Color.Gray.toArgb())
                setBackgroundColor(BluishGray.toArgb())

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEND && text.isNotBlank()) {
                        onSendClick()
                        imm.hideSoftInputFromWindow(windowToken, 0)
                        true
                    } else {
                        false
                    }
                }

                addTextChangedListener {
                    onTxtFieldValueChange(it?.toString() ?: "")
                }

                ViewCompat.setOnReceiveContentListener(this, arrayOf("image/*")) { _, payload ->
                    val uri = payload.clip.getItemAt(0).uri
                    onImageReceived(uri)
                    null
                }
            }

            // Mic Icon
            val micIcon = ImageView(context).apply {
                setImageResource(R.drawable.ic_mic)
                setColorFilter(Color.Gray.toArgb())
                setPadding(16, 0, 16, 0)
                setOnClickListener {

                }
            }

            //Clip Icon
            val clipIcon = ImageView(context).apply {
                setImageResource(R.drawable.ic_attach_file)
                setColorFilter(Color.Gray.toArgb())
                setPadding(16, 0, 32, 0)
                layoutParams = ViewGroup.LayoutParams(
                    50.dpToPx(context), 50.dpToPx(context)
                )
                setOnClickListener {
                    onClipIconClick()
                }
            }

            val sendIcon = ImageView(context).apply {
                setImageResource(R.drawable.ic_send)
                setColorFilter(TelegramBlueColor.toArgb())
                setPadding(16, 0, 32, 0)
                layoutParams = ViewGroup.LayoutParams(
                    40.dpToPx(context), 40.dpToPx(context)
                )
                setOnClickListener {
                    onSendClick()
                    imm.hideSoftInputFromWindow(editText.windowToken, 0)
                }
            }

            editTextRef = editText
            clipIconRef = clipIcon
            micIconRef = micIcon
            sendIconRef = sendIcon

            container.addView(emojiIcon)
            container.addView(editText)
            if (txtFieldValue.isBlank()) {
                container.addView(clipIcon)
                container.addView(micIcon)
            } else {
                container.addView(sendIcon)
            }

            container
        },

        update = { container ->
            val editText = editTextRef ?: return@AndroidView

            // Sync text field state
            if (editText.text.toString() != txtFieldValue) {
                editText.setText(txtFieldValue)
                editText.setSelection(txtFieldValue.length)
            }

            // Dynamically update trailing icons
            container.removeViews(2, container.childCount - 2)
            if (txtFieldValue.isBlank()) {
                clipIconRef?.let { container.addView(it) }
                micIconRef?.let { container.addView(it) }
            } else {
                sendIconRef?.let { container.addView(it) }
            }
        }
    )


}

fun Int.dpToPx(context: Context): Int =
    (this * context.resources.displayMetrics.density).toInt()