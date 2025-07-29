package com.kroy.sseditor.presentation.chat.ios.components


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
import com.kroy.sseditor.presentation.theme.TelegramDark

//@Preview(showSystemUi = true)
@Composable
fun ImageMessage(
    time: String = "12:45 AM",
    image: Bitmap? = null,
    isSender: Boolean = true,
    shouldShowChatTail: Boolean = true,
    lastReceiverMsgTime: String,
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (shouldShowChatTail) Modifier.padding(horizontal = 0.dp)
                else Modifier.padding(start = 8.dp, end = 14.dp)
            ),
        horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {

        if (!isSender && shouldShowChatTail) {
            SenderChatBubbleTriangleTail(
                modifier = Modifier
                    .size(17.dp, 17.dp)
                    .padding(bottom = 4.dp)
                    .offset(x = 8.dp),
                isSender = isSender,
                color = Color.Black
            )
        }

        val imageBitmap = image?.asImageBitmap()
        val width = (imageBitmap?.width) ?: 0
        val height = imageBitmap?.height ?: 0
        val isLandscape = width > height

        imageBitmap?.let {

            Box(
                modifier = Modifier
                    .padding(start = 1.dp, bottom = 1.dp)
                    .wrapContentSize()
//                    .then(
//                        if (isLandscape)
//                            Modifier
//                                .widthIn(200.dp, 300.dp)
//                                .heightIn(180.dp, 250.dp)
//                        else
//                            Modifier
//                                .widthIn(150.dp, 220.dp)
//                                .heightIn(230.dp, 350.dp)
//                    )
                    .clip(RoundedCornerShape(16.dp))

            ) {


                Image(
                    modifier = Modifier
                        .then(
                            if (isLandscape)
                                Modifier
                                    .widthIn(290.dp, 300.dp)
                                    .heightIn(210.dp, 230.dp)
//                                    .width(290.dp)
//                                    .height(210.dp)
                            else
                                Modifier
                                    .widthIn(230.dp, 290.dp)
                                    .heightIn(300.dp, 370.dp)
//                                    .width(290.dp)
//                                    .height(370.dp)
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(colors =if (isSender) listOf(
                                Color(0xFF201F24),
                                Color(0xFF252024),
                                Color(0xFF1B1A1F)
                             )
                            else listOf(Color.Black, Color.Black)),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    bitmap = image.asImageBitmap(),
                    contentDescription = "Chart Screenshot",
                    contentScale = if (isLandscape) ContentScale.FillBounds else ContentScale.Crop
                )


                // Time text with tick
                TimeWithTickBox(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    time = if (isSender) time else lastReceiverMsgTime,
                    isTextMessage = false,
                    isSender = isSender
                )
            }
        }


        if (isSender && shouldShowChatTail) {
            SenderChatBubbleTriangleTail(
                modifier = Modifier
                    .size(17.dp, 17.dp)
                    .padding(bottom = 4.dp)
                    .offset(x = (-7).dp)
                    .zIndex(-1f),
                isSender = isSender,
                color = if (isSender) TelegramDark else Color.Black
            )
        }

        // Forward icon placed outside the chart box
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