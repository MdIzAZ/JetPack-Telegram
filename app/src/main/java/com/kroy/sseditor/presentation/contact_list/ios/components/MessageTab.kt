package com.kroy.sseditor.presentation.contact_list.ios.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.presentation.theme.CustomRobotoMediumFontFamily
import com.kroy.sseditor.presentation.theme.Dimens
import com.kroy.sseditor.presentation.theme.IosFolderColor
import com.kroy.sseditor.presentation.theme.Telegram
import com.kroy.sseditor.presentation.theme.sse_editor_theme.TelegramCustomTheme

@Preview
@Composable
fun MessageTab(
    modifier: Modifier = Modifier,
    isSelected: Boolean = true,
    title: String = "Folder",
    count: Int = 4
) {

    val tColorScheme = TelegramCustomTheme.colorScheme

    Column(
        modifier = modifier.background(tColorScheme.topbarBackgroundColor1),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(bottom = 7.dp)
        ) {
            Text(
                text = title,
                color = if(isSelected) tColorScheme.selectedFolderColor else tColorScheme.unSelectedFolderColor,
                fontWeight = FontWeight.Bold,
                letterSpacing = (0.7f).sp,
                fontSize = 14.sp,
                fontFamily = CustomRobotoMediumFontFamily
            )

            BadgeBoxSmall(
                modifier = Modifier.padding(start = 4.dp),
                unreadCount = if (title == "All") 0 else count,
                color = if(isSelected) tColorScheme.selectedFolderColor else tColorScheme.unSelectedFolderColor
            )

        }

        Spacer(modifier = Modifier.height(5.dp))

        if (isSelected) {
            HorizontalDivider(
                modifier = Modifier
                    .width(80.dp)
                    .padding(top = 0.dp, end = 2.dp, start = 0.dp),
                thickness = 2.dp,
                color = tColorScheme.selectedFolderColor
            )
        }
    }

}