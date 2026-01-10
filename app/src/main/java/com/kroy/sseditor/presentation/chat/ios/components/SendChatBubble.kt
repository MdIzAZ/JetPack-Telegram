package com.kroy.sseditor.presentation.chat.ios.components

import android.annotation.SuppressLint
import android.text.TextPaint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kroy.sseditor.domain.models.ChatMessage
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.SenderTextColor
import com.kroy.sseditor.presentation.theme.TelegramDark
import com.kroy.sseditor.presentation.theme.sse_editor_theme.TelegramCustomTheme

@SuppressLint("UnusedBoxWithConstraintsScope")
@Preview(showBackground = true)
@Composable
fun SendChatBubble(
    modifier: Modifier = Modifier,
    shouldShowChatTail: Boolean = true,
    message: ChatMessage = ChatMessage(
        id = 0,
        recipientId = -1,
        isTextMessage = true,
        text = "To achieve a Jetpack Compose layout where two text items are in a row, placed at opposite ends, and the second item moves to the next line if there’s not enough space (e.g., when Text 1 is too long), while maintaining end-of-box alignment, we can use a Row with Arrangement.SpaceBetween and ensure wrapping behavior with proper modifiers. The previous Column approach didn’t prioritize keeping both items in a single row when space allows. Below is a revised solution using Row with wrapContentWidth and constraints to handle the wrapping dynamically.",
        timestamp = "05:07 AM",
        isSender = true
    )
) {

    val colorScheme = TelegramCustomTheme.colorScheme

    Row(
        modifier = modifier
            .padding(horizontal = 14.dp)
            .padding(bottom = if (shouldShowChatTail) 6.dp else 0.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(start = 0.dp)
        ) {


            // Main chat bubble
            BoxWithConstraints(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorScheme.senderChatBubbleColor1,
                                colorScheme.senderChatBubbleColor2
                            ) // example gradient (green tones)
                        ),
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 10.dp,
                            bottomEnd = 10.dp,
                            bottomStart = if (shouldShowChatTail) 20.dp else 20.dp
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .zIndex(1f)
            ) {


                val requiredWidthInDp = measureTextWidthInDp(
                    text = message.text + message.timestamp + "  ",
                    fontSizeSp = 17f
                )
                val width = constraints.maxWidth
                val density = LocalDensity.current
                val maxWidthInDp = with(density) { width.toDp() }

                if (requiredWidthInDp < maxWidthInDp) {

//                    Log.d("izaz", "$requiredWidthInDp S  $maxWidthInDp")
                    

                    Row(modifier = Modifier.wrapContentSize()) {
                        Text(
                            text = message.text,
                            fontFamily = CustomRobotoMediumFontFamily,
                            fontWeight = FontWeight.SemiBold,
//                            color = Color.White,
                            fontSize = 17.sp,
                            letterSpacing = (-0.5).sp,
                            maxLines = Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.wrapContentWidth()

                        )
                        TimeWithTickBox(
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .offset(x = 3.dp, y = 3.dp),
                            time = message.timestamp,
                            isTextMessage = true,
                            isSender = true
                        )
                    }

                } else {

//                    Log.d("izaz", "$requiredWidthInDp L  $maxWidthInDp")

                    Column(modifier = Modifier.wrapContentSize()) {

                        Text(
                            text = message.text,
                            fontFamily = CustomRobotoMediumFontFamily,
                            fontWeight = FontWeight.Thin,
//                            color = Color.White,
                            fontSize = 17.sp,
                            letterSpacing = (-0.5).sp,
                            maxLines = Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.wrapContentWidth()

                        )
                        TimeWithTickBox(
                            modifier = Modifier
                                .align(Alignment.End)
                                .offset(x = 3.dp, y = 3.dp),
                            time = message.timestamp,
                            isTextMessage = true,
                            isSender = true
                        )

                    }
                }
            }

            // Show triangle only if this is the last message
            if (shouldShowChatTail) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .graphicsLayer {
                            scaleX = -1f
                        }
                        .offset(x = (-8).dp)
                ) {
                    SenderChatBubbleTriangleTail(
                        modifier = Modifier
                            .size(14.dp, 12.dp)
                            .offset(x = .1.dp)

                            .zIndex(-1f),

                        isSender = false,
                        color =  Color(0xFF7B1AB9)
                    )
                }
            }
        }
    }
}





@Composable
fun measureTextWidthInDp(
    text: String,
    fontSizeSp: Float
): Dp {
    val density = LocalDensity.current

    val textPaint = remember(fontSizeSp) {
        TextPaint().apply {
            isAntiAlias = true
            textSize = with(density) { fontSizeSp.sp.toPx() }
        }
    }

    val widthPx = textPaint.measureText(text)

    return with(density) { widthPx.toDp() }
}



