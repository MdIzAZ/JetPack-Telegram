package com.kroy.sseditor.presentation.chat.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.domain.models.ChatMessage
import com.kroy.sseditor.presentation.chat.ios.components.TimeWithTickBox
import com.kroy.sseditor.presentation.chat.ios.components.measureTextWidthInDp
import com.kroy.sseditor.presentation.theme.BluishGray
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily

@Preview(showSystemUi = true)
@Composable
fun AndroidReceivedChatBubble(
    modifier: Modifier = Modifier,
    shouldShowChatTail: Boolean = true,
    lastReceiverMsgTime: String = "03:22 AM",
    message: ChatMessage = ChatMessage(
        id = 34,
        recipientId = -2,
        isTextMessage = true,
        text = "Hello , How are you? Hello , How are you? Hello , How are you?Hello , How are you?vvvHello , How are you?vvvv",
        timestamp = "05:07 AM",
        isSender = false
    ),
    onLongPress: () -> Unit = {}
) {


    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = if (shouldShowChatTail) 6.dp else 2.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .padding(start = 2.dp)
        ) {
            // Main chat bubble
            BoxWithConstraints(
                modifier = Modifier
                    .background(
                        color = BluishGray,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 16.dp,
                            bottomEnd = 16.dp,
                            bottomStart = if (shouldShowChatTail) 0.dp else 8.dp
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                onLongPress()
                            }
                        )
                    }

            ) {

                val requiredWidthInDp = measureTextWidthInDp(
                    text = message.text + message.timestamp + "  ",
                    fontSizeSp = 15f
                )
                val width = constraints.maxWidth
                val density = LocalDensity.current
                val maxWidthInDp = with(density) { width.toDp() }

                if (requiredWidthInDp < maxWidthInDp) {

                    Row() {
                        Text(
                            text = message.text,
                            fontFamily = CustomRobotoMediumFontFamily,
                            fontWeight = FontWeight.Thin,
                            color = Color.White,
                            fontSize = 15.sp,
                            letterSpacing = (-0.5).sp,
                            maxLines = Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.wrapContentWidth()

                        )
                        TimeWithTickBox(
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .offset(x = 2.dp, y = 1.dp),
                            time = lastReceiverMsgTime,
                            isTextMessage = true,
                            isSender = false
                        )
                    }

                } else {

                    Column {

                        Text(
                            text = message.text,
                            fontFamily = CustomRobotoMediumFontFamily,
                            fontWeight = FontWeight.Thin,
                            color = Color.White,
                            fontSize = 15.sp,
                            letterSpacing = (-0.5).sp,
                            maxLines = Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.wrapContentWidth()

                        )
                        TimeWithTickBox(
                            modifier = Modifier
                                .align(Alignment.End),
                            time = lastReceiverMsgTime,
                            isTextMessage = true,
                            isSender = false
                        )
                    }
                }

            }


            if (shouldShowChatTail) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-10).dp)
                ) {
                    AndroidChatTail(
                        modifier = Modifier
                            .size(16.dp, 16.dp),
                        isSender = false,
                        color = BluishGray
                    )
                }
            }
        }
    }
}