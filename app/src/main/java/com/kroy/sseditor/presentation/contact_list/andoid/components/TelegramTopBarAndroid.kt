package com.kroy.sseditor.presentation.contact_list.andoid.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.presentation.contact_list.defaultFolderList
import com.kroy.sseditor.presentation.contact_list.ios.components.MessageTab
import com.kroy.sseditor.presentation.theme.CustomMediumTypography
import com.kroy.sseditor.presentation.theme.DARK_GRAY
import com.kroy.sseditor.presentation.theme.IosFolderColor

@Preview(showBackground = true, backgroundColor = 0x00000000)
@Composable
fun TelegramTopBarAndroid(
    modifier: Modifier = Modifier,
    unreadMessageCount: Int = 4,
    folders: List<Pair<String, Int>> = defaultFolderList
) {


    Column {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
                .background(DARK_GRAY),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Menu,
                    tint = Color.White,
                    contentDescription = "Menu"
                )

                Text(
                    text = "Telegram",
                    color = Color.White,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp,
                    style = CustomMediumTypography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 24.dp)
                )
            }

            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.Search,
                tint = Color.White,
                contentDescription = "Search"
            )

        }


        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(
                    top = 8.dp,
                    bottom = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            item {
                Spacer(Modifier.width(4.dp))
            }

            val spacerValue = 8.dp
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