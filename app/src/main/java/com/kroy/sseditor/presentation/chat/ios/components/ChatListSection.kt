package com.kroy.sseditor.presentation.chat.ios.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kroy.sseditor.domain.models.ChatMessage
import com.kroy.sseditor.domain.models.NonTextMessage
import com.kroy.sseditor.domain.models.dummyChatMessages
import com.kroy.sseditor.presentation.theme.CustomMediumTypography
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ChatListSection(
    modifier: Modifier = Modifier,
    lastReceiverMsgTime: String,
    chats: List<ChatMessage>,
    onLongPress: (id: Int) -> Unit
) {

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
        val lastIndex = if(chats.lastIndex < 0) 0 else chats.lastIndex
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

    Box(modifier = modifier) {


        if (shouldShowToday && !isAtTop) {
            Text(
                text = "Today",
                style = CustomMediumTypography.titleMedium,
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(4.dp)
                    .padding(top = 2.dp)
                    .background(Color(0x65000000), RoundedCornerShape(10.dp))
                    .padding(horizontal = 5.dp, vertical = (2.7f).dp)
                    .zIndex(1f)
            )
        }

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState,
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
                            .padding(4.dp)
                            .padding(top = 2.dp)
                            .background(Color(0x65000000), RoundedCornerShape(10.dp))
                            .padding(horizontal = 5.dp, vertical = (2.7f).dp)
                            .zIndex(1f)
                    )
                }
            }

            itemsIndexed(chats) { idx, item ->

                val shouldShowChatTail =
                    if (chats.lastIndex == idx) true
                    else if (!(item.isSender xor chats[idx + 1].isSender)) false
                    else true


                when (item.isTextMessage) {

                    true -> {
                        if (item.isSender) {
                            SendChatBubble(
                                modifier = Modifier.fillMaxWidth(),
                                shouldShowChatTail = shouldShowChatTail,
                                message = item
                            )
                        } else {
                            ReceivedChatBubble(
                                modifier = Modifier.fillMaxWidth(),
                                shouldShowChatTail = shouldShowChatTail,
                                message = item,
                                lastReceiverMsgTime = receiverTimeMap[idx] ?: "12:00 PM",
                                onLongPress = {onLongPress(item.id)}
                            )
                        }
                    }

                    false -> {
                        when (item.nonTextMessage) {
                            is NonTextMessage.Image -> ImageMessage(
                                time = item.timestamp,
                                image = item.nonTextMessage.bitmap,
                                isSender = item.isSender,
                                shouldShowChatTail = shouldShowChatTail,
                                lastReceiverMsgTime = receiverTimeMap[idx] ?: "12:00 PM"
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

                Spacer(Modifier.height(1.dp))
            }


        }


    }
}


@Preview
@Composable
fun PreviewChatListSection(modifier: Modifier = Modifier) {
    ChatListSection(chats = dummyChatMessages, lastReceiverMsgTime = "03:55 AM" ,onLongPress = {})
}