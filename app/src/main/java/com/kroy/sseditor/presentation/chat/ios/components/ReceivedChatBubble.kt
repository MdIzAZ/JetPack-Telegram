package com.kroy.sseditor.presentation.chat.ios.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.domain.models.ChatMessage
import com.kroy.sseditor.presentation.theme.ChatBubbleGray
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily

@Preview(showSystemUi = true)
@Composable
fun ReceivedChatBubble(
    modifier: Modifier = Modifier,
    shouldShowChatTail: Boolean = true,
    lastReceiverMsgTime: String = "09:11 PM",
    message: ChatMessage = ChatMessage(
        id = 0,
        recipientId = -1,
        isTextMessage = true,
        text = "Bhai bina loss ke aaj ka session complete hua",
        timestamp = "05:07 AM",
        isSender = false
    ),
    onLongPress: () -> Unit = {}
) {

    Row(
        modifier = modifier
//            .alpha(.9f)
            .padding(start = 8.dp, end = 14.dp)
            .padding(bottom = if (shouldShowChatTail) 6.dp else 0.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(.85f)
                .padding(start = 2.dp)
        ) {
            // Main chat bubble
            BoxWithConstraints(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(listOf(Color(0xFF342525),Color(0xFF2E2133) ,Color(0xFF2E2133) )),
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 18.dp,
                            bottomEnd = 18.dp,
                            bottomStart = if (shouldShowChatTail) 8.dp else 8.dp
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

                    Row(modifier = Modifier.padding(end = 0.dp)) {
                        Text(
                            text = message.text,
                            fontFamily = CustomRobotoMediumFontFamily,
                            fontWeight = FontWeight.Thin,
                            color = Color.White,
                            fontSize = 17.sp,
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

                    Column(modifier = Modifier.wrapContentWidth()) {

                        Text(
                            text = message.text,
                            fontFamily = CustomRobotoMediumFontFamily,
                            fontWeight = FontWeight.W500,
                            color = Color.White,
                            fontSize = 17.sp,
                            letterSpacing = (-0.5).sp,
                            maxLines = Int.MAX_VALUE,
                            modifier = Modifier.wrapContentWidth()

                        )
                        TimeWithTickBox(
                            modifier = Modifier
                                .padding(end = 6.dp)
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
                    ReceiverChatBubbleTriangleTail(
                        modifier = Modifier
                            .size(12.dp, 10.dp)
                            .offset(x = 2.dp),
                        isSender = false,
                        color = Color.Black.copy(.6f)
                    )
                }
            }
        }
    }
}