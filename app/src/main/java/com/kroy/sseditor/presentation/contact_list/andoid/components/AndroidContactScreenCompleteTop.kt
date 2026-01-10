package com.kroy.sseditor.presentation.contact_list.andoid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.contact_list.defaultFolderList
import com.kroy.sseditor.presentation.theme.BluishGray
import com.kroy.sseditor.presentation.theme.DARK_GRAY
import com.kroy.sseditor.presentation.theme.sse_editor_theme.TelegramCustomTheme

@Preview
@Composable
fun AndroidContactScreenCompleteTop(
    modifier: Modifier = Modifier,
    time: String = "04:25 PM",
    folder: List<Pair<String, Int>> = defaultFolderList,
    battery: Pair<Int, Int> = Pair(R.drawable.ic_battery, 58),
    unreadMessageCount: Int = 4
) {
    Column(
        modifier = modifier
            .background(TelegramCustomTheme.colorScheme.topbarBackgroundColor1)
            .fillMaxWidth()
    ) {
        AndroidNotificationBar(
            modifier = Modifier,
            time = time,
            battery = battery.second
        )

        Spacer(
            Modifier
                .height(8.dp)
        )
        TelegramTopBarAndroid(unreadMessageCount = unreadMessageCount, folders = folder)
    }
}

