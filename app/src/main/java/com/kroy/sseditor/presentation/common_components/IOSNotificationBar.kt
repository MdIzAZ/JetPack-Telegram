package com.kroy.sseditor.presentation.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.theme.CustomMediumTypography
import com.kroy.sseditor.presentation.theme.CustomRegularFontFamily
import com.kroy.sseditor.presentation.theme.sse_editor_theme.TelegramCustomTheme
import com.kroy.sseditor.utils.SelectedClient
import com.kroy.sseditor.utils.Utils
import com.kroy.sseditor.utils.Utils.removeLeadingZero

@Composable
fun IOSNotificationBar(
    modifier: Modifier = Modifier,
    batteryIcon: Int = R.drawable.battery70,
    batteryPercentage: Int = 70,
    time: String,
    onLongPress: () -> Unit
) {

    val colorScheme = TelegramCustomTheme.colorScheme


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(brush = Brush.linearGradient(
                listOf(
                    TelegramCustomTheme.colorScheme.topbarBackgroundColor1,
                    TelegramCustomTheme.colorScheme.topbarBackgroundColor2,
                    TelegramCustomTheme.colorScheme.topbarBackgroundColor3,
                )
            ))
            .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, bottom = 6.dp, top = 8.dp)
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = removeLeadingZero(time).dropLast(2).trim(),
                color = colorScheme.topbarContentColor,
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                style = CustomMediumTypography.titleMedium,
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(80.dp, 18.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_signal2),
                    contentDescription = "Signal",
                    tint = colorScheme.topbarContentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_wifi),
                    contentDescription = "Signal",
                    tint = colorScheme.topbarContentColor,
                    modifier = Modifier.size(18.dp)
                )
                //   Icon(painterResource(id = R.drawable.ic_wifi), contentDescription = "Wi-Fi", tint = colorScheme.topbarContentColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(1.dp))


                Icon(
                    modifier = Modifier
                        .size(34.dp, 12.dp),
                    painter = painterResource(batteryIcon),
                    contentDescription = "Battery",
                    tint = colorScheme.topbarContentColor
                )

                Spacer(modifier = Modifier.width(16.dp))

            }
        }

        DynamicIsland(modifier = Modifier.align(Alignment.Center), onLongPress = { onLongPress() })
    }


}


@Preview
@Composable
fun PrevIOSNotificationBar(modifier: Modifier = Modifier) {
    IOSNotificationBar(
        modifier = modifier,
        time = "09:11 PM",
        onLongPress = {}
    )
}