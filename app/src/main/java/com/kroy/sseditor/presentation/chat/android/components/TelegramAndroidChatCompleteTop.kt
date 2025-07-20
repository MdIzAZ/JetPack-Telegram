package com.kroy.sseditor.presentation.chat.android.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.contact_list.andoid.components.AndroidNotificationBar
import com.kroy.sseditor.presentation.theme.BluishGray

@Composable
fun TelegramAndroidChatCompleteTop(
    modifier: Modifier = Modifier,
    time: String = "04:25 PM",
    battery:Pair<Int,Int> = Pair(R.drawable.ic_battery, 58),
    name: String = "Roberto Baggio",
    profilePic: Bitmap? = null,
    color: Color? = null,
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BluishGray)
    ) {

        AndroidNotificationBar(
            time = time,
            battery = battery.second
        )


        AndroidChatScreenTopBar(
            name = name,
            profilePic = profilePic,
            color = color,
            onBackClick = onBackClick
        )

    }
}