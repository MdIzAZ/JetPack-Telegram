package com.kroy.sseditor.presentation.contact_list.andoid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.LtePlusMobiledata
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.theme.BluishGray
import com.kroy.sseditor.presentation.theme.CustomGray
import com.kroy.sseditor.presentation.theme.CustomMediumTypography
import com.kroy.sseditor.presentation.theme.DARK_GRAY
import com.kroy.sseditor.utils.Utils.removeLeadingZero

@Preview(showBackground = true)
@Composable
fun AndroidNotificationBar(
    modifier: Modifier = Modifier,
    time: String = "04:25 PM",
    battery: Int = 56
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = DARK_GRAY)
            .padding(top = 12.dp, end = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = removeLeadingZero(time).dropLast(2).trim(),
            color = Color.White,
            fontSize = 14.sp,
            letterSpacing = 1.sp,
            modifier = Modifier
                .padding(start = 20.dp)
                .size(80.dp, 18.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Default.LtePlusMobiledata,
                tint = Color.White,
                contentDescription = "LTE"
            )

            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.NetworkCell,
                tint = Color.White,
                contentDescription = "Network"
            )

            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Default.Battery6Bar,
                tint = Color.White,
                contentDescription = "Battery"
            )

            Spacer(Modifier.width(8.dp))

//            Text(
//                text = "$battery%",
//                color = Color.White,
//                fontSize = 13.sp,
//                letterSpacing = 1.sp,
//                modifier = Modifier.padding(end = 4.dp)
//            )


        }

    }

}