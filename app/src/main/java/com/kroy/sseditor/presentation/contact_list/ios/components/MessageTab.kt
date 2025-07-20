package com.kroy.sseditor.presentation.contact_list.ios.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.Dimens
import com.kroy.sseditor.presentation.theme.Telegram

@Composable
fun MessageTab(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    title: String,
    count: Int
) {

    Column(
        modifier = modifier.height(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 7.dp)
        ) {
            Text(
                text = title,
                color = Telegram,
                fontWeight = FontWeight.Bold,
                letterSpacing = (0.5f).sp,
                fontSize = 16.sp,
                fontFamily = CustomRobotoMediumFontFamily
            )

            BadgeBoxSmall(
                modifier = Modifier.padding(start = 4.dp),
                unreadCount = count
            )

        }

        if (isSelected) {
            HorizontalDivider(
                modifier = Modifier
                    .width(80.dp)  // Adjust width based on content
                    .padding(top = 0.dp, end = 2.dp, start = 0.dp),
                thickness = 2.dp,
                color = Telegram
            )
        }
    }

}