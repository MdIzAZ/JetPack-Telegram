package com.kroy.sseditor.presentation.sevenday

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kroy.sseditor.domain.models.ClientTimes
import com.kroy.sseditor.domain.models.IntervalGroup
import com.kroy.sseditor.domain.models.OSType
import com.kroy.sseditor.domain.models.ThemeMode
import com.kroy.sseditor.presentation.common_components.CustomTopBar
import com.kroy.sseditor.presentation.sevenday.components.DayWithTimePicker


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SevenDayScreen(
    state: SevenDayScreenState,
    currentOSType: OSType,
    updateTriggerTime: (time: String, dayIndex: Int) -> Unit,
    updateUiTime: (time: String, dayIndex: Int) -> Unit,
    onGoClicked: (clientTimes: ClientTimes, dayName: String) -> Unit,
    onThemeSelected: (ThemeMode) -> Unit,
    setFolders: (List<Pair<String, Int>>) -> Unit,
    onCheckedChange: (OSType) -> Unit,
    onSaveIntervals: (List<IntervalGroup>) -> Unit,
) {

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            CustomTopBar(
                title = "Client : ${state.name}",
                onThemeSelected = onThemeSelected,
                setFolders = setFolders,
                currentOSType = currentOSType,
                onOsTypeChange = onCheckedChange
            )
        }
    ) { ip ->

        Column(
            modifier = Modifier
                .padding(ip)
                .fillMaxSize()
                .padding(16.dp),
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                items(7) { dayIndex ->
                    DayWithTimePicker(
                        day = (dayIndex + 1).toString(),
                        clientTimes = state.times[dayIndex],
                        onTimeSelected = { newTime, timeName ->
                            when (timeName) {
                                0 -> updateTriggerTime(newTime, dayIndex)
                                1 -> updateUiTime(newTime, dayIndex)
                            }
                        },
                        onGoClicked = { clientTimes, dayName -> onGoClicked(clientTimes, dayName) },
                        onSaveIntervals = onSaveIntervals
                    )
                }
            }
        }


        if (state.isLoading) {

            Box(modifier = Modifier.fillMaxSize()) {
                ElevatedCard(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text("Fetching contacts..")

                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .size(120.dp),
                            strokeWidth = 12.dp,
                            strokeCap = StrokeCap.Butt
                        )
                    }
                }
            }

        }

    }


}


@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(hour: Int, minute: Int): String {
    val amPm = if (hour < 12) "AM" else "PM"
    val formattedHour = if (hour % 12 == 0) 12 else hour % 12
    return String.format("%02d:%02d %s", formattedHour, minute, amPm)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun Preview4() {
    SevenDayScreen(
        state = SevenDayScreenState(),
        currentOSType = OSType.IOS,
        updateTriggerTime = { _, _ -> },
        updateUiTime = { _, _ -> },
        onGoClicked = { _, _ -> },
        onThemeSelected = {},
        onCheckedChange = {},
        setFolders = {},
        onSaveIntervals = {}
    )
}













