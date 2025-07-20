package com.kroy.sseditor.presentation.sevenday.components

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.domain.models.ClientTimes
import com.kroy.sseditor.domain.models.IntervalGroup
import com.kroy.sseditor.presentation.sevenday.formatTime
import com.kroy.sseditor.presentation.theme.CustomBoldTypography
import com.kroy.sseditor.presentation.theme.LightBlue
import com.kroy.sseditor.presentation.theme.TelegramLight
import com.kroy.sseditor.utils.SelectedClient.clientTimes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayWithTimePicker(
    day: String,
    clientTimes: ClientTimes,
    onTimeSelected: (String, Int) -> Unit,
    onSaveIntervals: (List<IntervalGroup>) -> Unit,
    onGoClicked: (clientTimes: ClientTimes, dayName: String) -> Unit
) {

    var shouldShowIntervalDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var currentTimeType by remember { mutableStateOf(0) } /* 0 for time1, 1 for time2 */
    val now = remember { Calendar.getInstance() }
    val context = LocalContext.current


    // Show time picker dialog if needed
    if (showTimePicker) {
        CustomTimePickerDialog(
            onConfirm = { selectedHour, selectedMinute ->


                if (currentTimeType == 0) {
                    if (!isFutureTime(selectedHour, selectedMinute, now)) {
                        Toast.makeText(context, "Select a future time", Toast.LENGTH_SHORT).show()
                        return@CustomTimePickerDialog
                    }
                }

                onTimeSelected(formatTime(selectedHour, selectedMinute), currentTimeType)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    if (shouldShowIntervalDialog) {
        IntervalSelectionDialog(
            onSave = {
                onSaveIntervals(it)
                shouldShowIntervalDialog = false
            },
            onDismiss = { shouldShowIntervalDialog = false }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = "D$day :",
            modifier = Modifier.weight(.2f),
            style = CustomBoldTypography.titleMedium,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )


        LazyRow(
            modifier = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {

            item {
                TimeSelectionButton(
                    modifier = Modifier,
                    title = "Trigger Time",
                    time = clientTimes.triggerTime,
                    onTimerBtnClick = {
                        currentTimeType = 0
                        showTimePicker = true
                    }
                )
            }

            item {
                TimeSelectionButton(
                    modifier = Modifier,
                    title = "UI Time",
                    time = clientTimes.uiTime,
                    onTimerBtnClick = {
                        currentTimeType = 1
                        showTimePicker = true
                    }
                )
            }

            item {
//                IntervalSelection(
//                    interval = clientTimes.interval,
//                    onIncrease = onIncrease,
//                    onDecrease = onDecrease,
//                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = {
                            shouldShowIntervalDialog = true
                        },
                        content = { Text("Interval") }
                    )
                }

            }


        }


        // Go button
        IconButton(
            modifier = Modifier.weight(.15f),
            onClick = {
                if (
                    !isFutureTime(
                        parseTimeTo24Hour(clientTimes.triggerTime).first,
                        parseTimeTo24Hour(clientTimes.triggerTime).second,
                        now
                    )
                ) {
                    Toast.makeText(context, "Select a future trigger time", Toast.LENGTH_SHORT)
                        .show()
                    return@IconButton
                }

                onGoClicked(clientTimes, "Day $day")
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                tint = Color.Black,
                contentDescription = "Go"
            )
        }
    }
}


private fun isFutureTime(hour: Int, minute: Int, now: Calendar): Boolean {

    val selected = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val isTimeValid = selected.timeInMillis >= now.timeInMillis

    return isTimeValid

}


fun parseTimeTo24Hour(timeStr: String): Pair<Int, Int> {
    val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val date = inputFormat.parse(timeStr) ?: return 0 to 0

    val calendar = Calendar.getInstance().apply {
        time = date
    }

    val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    return hour24 to minute
}