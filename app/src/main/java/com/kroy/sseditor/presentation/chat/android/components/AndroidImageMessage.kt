package com.kroy.sseditor.presentation.chat.android.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.chat.ios.components.SenderChatBubbleTriangleTail
import com.kroy.sseditor.presentation.chat.ios.components.TimeWithTickBox
import com.kroy.sseditor.presentation.theme.BluishGray
import com.kroy.sseditor.presentation.theme.DarkPink
import com.kroy.sseditor.presentation.theme.TelegramDark
import com.kroy.sseditor.utils.Utils

@Preview(showSystemUi = true)
@Composable
fun AndroidImageMessage(
    modifier: Modifier = Modifier,
    time: String = "12:45 AM",
    image: Bitmap? = null,
    isSender: Boolean = false,
    borderColor: Color = DarkPink,
    lastReceiverMsgTime: String = "09:11 AM",
    shouldShowChatTail: Boolean = true
) {

    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .padding(horizontal = 0.dp)
        /*
        .then(
            if (shouldShowChatTail) Modifier.padding(horizontal = 0.dp)
            else Modifier.padding(horizontal = 16.dp)
        )
        */,
        horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {


        val imageBitmap = image?.asImageBitmap()
        val width = (imageBitmap?.width) ?: 0
        val height = imageBitmap?.height ?: 0
        val isLandscape = width > height


        imageBitmap?.let {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (!isSender && !shouldShowChatTail) 0.dp else 16.dp,
                            bottomEnd = if (isSender && !shouldShowChatTail) 0.dp else 16.dp
                        )
                    )

            ) {

                // Main image

                Image(
                    modifier = Modifier
                        .then(
                            if (isLandscape)
                                Modifier
                                    .widthIn(20.dp, 300.dp)
                                    .heightIn(18.dp, 250.dp)
                            else
                                Modifier
                                    .widthIn(150.dp, 220.dp)
                                    .heightIn(230.dp, 350.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = if (isSender) borderColor else BluishGray,
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (!isSender && !shouldShowChatTail) 0.dp else 16.dp,
                                bottomEnd = if (isSender && !shouldShowChatTail) 0.dp else 16.dp,
                            )
                        ),
                    bitmap = imageBitmap,
                    contentDescription = "Chart Screenshot",
                    contentScale = if (isLandscape) ContentScale.FillWidth else ContentScale.Crop
                )


                // Time text with tick
                TimeWithTickBox(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    time = if (!isSender)lastReceiverMsgTime else time,
                    isTextMessage = false,
                    isSender = isSender
                )
            }
        }


        if (!isSender)
            Box(
                modifier = Modifier
                    .align(Alignment.Bottom) // Align to the bottom of the row
                    .padding(start = 4.dp, bottom = 4.dp)
                    .size(36.dp) // Circular size for the forward icon background
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f), // Very light version of background
                                Color.Black.copy(alpha = 0.35f), // Very light version of background

                            ),
                            radius = 60f
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_forward), // Replace with your forward icon resource
                    contentDescription = "Telegram Forward",
                    modifier = Modifier
                        .size(20.dp) // Adjust size for the forward icon
                        .align(Alignment.Center) // Center the forward icon
                        .background(Color.Transparent),
                    colorFilter = ColorFilter.tint(Color.White) // Make forward icon white
                )
            }
    }
}