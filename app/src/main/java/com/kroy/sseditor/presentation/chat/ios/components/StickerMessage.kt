package com.kroy.sseditor.presentation.chat.ios.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kroy.ssediotor.R
import com.kroy.sseditor.utils.Utils


//@Preview(showSystemUi = true)
@Composable
fun StickerMessage(
    modifier: Modifier = Modifier,
    time: String = "09:32 PM",
    sticker: Bitmap,
    lastReceiverMsgTime: String,
    isSender: Boolean = false
) {

    val context = LocalContext.current
//    val imageBitmap = Utils.getBitmapFromResource(context, sticker)

    Row(
        modifier = modifier
            .padding(horizontal = 14.dp)
            .fillMaxSize(),
        horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .wrapContentSize()
        ) {

            Image(
                bitmap = sticker.asImageBitmap(),
                contentDescription = "Sticker",
                modifier = Modifier
                    .heightIn(min = 140.dp, max = 180.dp)
                    .widthIn(120.dp, 175.dp),
                contentScale = ContentScale.FillBounds
            )


            TimeWithTickBox(
                modifier = Modifier.align(Alignment.BottomEnd),
                isTextMessage = false,
                isSender = isSender,
                time = if (isSender) time else lastReceiverMsgTime
            )
        }
    }
}