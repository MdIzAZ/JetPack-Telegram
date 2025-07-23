package com.kroy.sseditor.presentation.chat.android.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kroy.sseditor.domain.models.ChatMessage
import com.kroy.sseditor.domain.models.NonTextMessage
import com.kroy.sseditor.presentation.chat.ios.components.StickerMessage
import com.kroy.sseditor.presentation.theme.CustomMediumTypography
import com.kroy.sseditor.presentation.theme.DarkPink
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AndroidChatListSection(
    modifier: Modifier = Modifier,
    lastReceiverMsgTime: String,
    chats: List<ChatMessage>,
    onLongPress: (id: Int) -> Unit
) {
    val context = LocalContext.current
    val screenHeightPx = context.resources.displayMetrics.heightPixels.toFloat()

    val listState = rememberLazyListState()
    var shouldShowToday by remember { mutableStateOf(false) }
    val isAtTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 &&
                    listState.firstVisibleItemScrollOffset == 0
        }
    }

    val receiverTimeMap = remember(chats) {
        val total = chats.count { !it.isSender }
        val map = mutableMapOf<Int, String>()
        var receiverSeen = 0
        val lastTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(lastReceiverMsgTime) ?: Date()

        chats.forEachIndexed { index, chat ->
            if (!chat.isSender) {
                receiverSeen++
                val group = (total - receiverSeen) / 3
                val cal = Calendar.getInstance().apply { time = lastTime }
                cal.add(Calendar.MINUTE, -group)
                map[index] = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time)
            }
        }

        map
    }

    LaunchedEffect(chats.size) {
        val lastIndex = if (chats.lastIndex < 0) 0 else chats.lastIndex
        listState.animateScrollToItem(lastIndex)
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            shouldShowToday = true
        } else {
            delay(1500)
            shouldShowToday = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        if (shouldShowToday && !isAtTop) {
            Text(
                text = "Today",
                style = CustomMediumTypography.titleMedium,
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
                    .padding(top = 6.dp)
                    .background(Color(0x65000000), RoundedCornerShape(10.dp))
                    .padding(horizontal = 5.dp, vertical = 2.7.dp)
                    .zIndex(1f)
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            item {
                if (isAtTop) {
                    Text(
                        text = "Today",
                        style = CustomMediumTypography.titleMedium,
                        color = Color.White,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(8.dp)
                            .padding(top = 6.dp)
                            .background(Color(0x65000000), RoundedCornerShape(10.dp))
                            .padding(horizontal = 5.dp, vertical = 2.7.dp)
                            .zIndex(1f)
                    )
                }
            }

            itemsIndexed(chats) { idx, item ->
                val shouldShowChatTail =
                    if (chats.lastIndex == idx) true
                    else if (!(item.isSender xor chats[idx + 1].isSender)) false
                    else true

                var itemOffsetY by remember { mutableFloatStateOf(0f) }
                val blendRatio = (itemOffsetY / screenHeightPx).coerceIn(0f, 1f)
                val bgColor = lerp(DarkPink, Color(0xFF495ED4), blendRatio)

                when (item.isTextMessage) {
                    true -> {
                        if (item.isSender) {
                            AndroidSendChatBubble(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned {
                                        itemOffsetY = it.localToWindow(Offset.Zero).y
                                    },
                                shouldShowChatTail = shouldShowChatTail,
                                itemColor = bgColor,
                                message = item
                            )
                        } else {
                            AndroidReceivedChatBubble(
                                modifier = Modifier.fillMaxWidth(),
                                shouldShowChatTail = shouldShowChatTail,
                                lastReceiverMsgTime = receiverTimeMap[idx] ?: "12:00 PM",
                                message = item,
                                onLongPress = {
                                    onLongPress(item.id)
                                    Log.d("izaz", "2: ${chats.find { it.id == item.id }}")
                                }
                            )
                        }
                    }

                    false -> {
                        when (item.nonTextMessage) {
                            is NonTextMessage.Image -> AndroidImageMessage(
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .onGloballyPositioned {
                                        itemOffsetY = it.localToWindow(Offset.Zero).y
                                    },
                                time = item.timestamp,
                                image = item.nonTextMessage.bitmap,
                                isSender = item.isSender,
                                borderColor = bgColor,
                                lastReceiverMsgTime = receiverTimeMap[idx] ?: "12:00 PM",
                                shouldShowChatTail = shouldShowChatTail
                            )

                            is NonTextMessage.Sticker -> StickerMessage(
                                time = item.timestamp,
                                sticker = item.nonTextMessage.bitmap,
                                isSender = item.isSender,
                                lastReceiverMsgTime = receiverTimeMap[idx] ?: "12:00 PM"
                            )

                            null -> {}
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewChatListSection(modifier: Modifier = Modifier) {
    AndroidChatListSection(chats = emptyList(), lastReceiverMsgTime = "12:00 PM", onLongPress = {})
}
