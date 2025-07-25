package com.kroy.sseditor.presentation.contact_list.ios.components

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.common_components.IOSNotificationBar
import com.kroy.sseditor.presentation.contact_list.defaultFolderList
import com.kroy.sseditor.presentation.theme.COLOR_PINK
import com.kroy.sseditor.presentation.theme.CustomComfortaaFontFamily
import com.kroy.sseditor.presentation.theme.CustomGray
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.IosFolderColor
import com.kroy.sseditor.presentation.theme.TelegramDark
import com.kroy.sseditor.presentation.theme.UnreadMessages
import org.json.JSONObject

@Preview
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactListScreenTopBar(
    time: String = "08:45 PM",
    unreadMessageCount: Int = 12,
    folders: List<Pair<String, Int>> = defaultFolderList,
    batteryIcon: Int = R.drawable.battery70,
    batteryPercentage: Int = 70,
    onLongPress: () -> Unit = {},
) {


    Column(
        modifier = Modifier
            .background(color = CustomGray)
            .fillMaxWidth()
    ) {

        IOSNotificationBar(
            modifier = Modifier.padding(8.dp),
            time = time,
            onLongPress = onLongPress,
            batteryPercentage = batteryPercentage,
            batteryIcon = batteryIcon
        )

        // Second Row - Telegram Title & Unread Tabs
        Row(
            modifier = Modifier
//                .padding(start = 10.dp)
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Edit",
                fontFamily = CustomRobotoMediumFontFamily,
                color = UnreadMessages, // Assuming you have defined TelegramDark
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(24.dp))
                Text(
                    text = "Chats",
                    color = Color.White,
                    fontFamily = CustomComfortaaFontFamily,
                    fontSize = 18.sp,
                    letterSpacing = 0.5f.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                Image(
                    painter = painterResource(id = R.drawable.bluetick1),
                    contentDescription = "Telegram Logo",
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_dotted),
                    contentDescription = "Search",
                    tint = IosFolderColor,
                    modifier = Modifier
                        .rotate(180f)
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "More options",
                    tint = IosFolderColor,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))


        // Third Row - Tabs with "Unread" centered vertically
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
//                .height(52.dp)
                .wrapContentHeight()
                .padding(top = 8.dp, start = 15.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            val spacerValue = 2.dp
            items(folders) {
                Spacer(modifier = Modifier.width(spacerValue))
                MessageTab(
                    title = it.first,
                    isSelected = it.first == "Unread",
                    count = if (it.first == "Unread") unreadMessageCount else it.second
                )
            }

        }
    }
}


fun readJsonFromUri(context: Context, uri: Uri): List<Pair<String, Int>> {
    val result = mutableListOf<Pair<String, Int>>()

    try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            val jsonString = input.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val keys = jsonObject.keys()

            while (keys.hasNext()) {
                val key = keys.next()
                val value = jsonObject.getInt(key)
                result.add(Pair(key, value))
            }

            Log.d("izaz", "Parsed JSON: $result")
        }
    } catch (e: Exception) {
        Log.e("izaz", "Error reading picked JSON: ${e.message}", e)
    }

    return result
}
    












