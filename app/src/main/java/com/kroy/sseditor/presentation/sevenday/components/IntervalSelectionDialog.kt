package com.kroy.sseditor.presentation.sevenday.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.domain.models.IntervalGroup

@Preview(showSystemUi = true)
@Composable
fun IntervalSelectionDialog(
    modifier: Modifier = Modifier,
    onSave: (List<IntervalGroup>) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current
    val groups = remember { mutableStateListOf(IntervalGroup(0, Int.MAX_VALUE, 0)) }


    AlertDialog(
        modifier = modifier,
        containerColor = colorScheme.surface,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Select Groups",
                    color = colorScheme.onSurface,
                    style = typography.headlineSmall
                )

                Row {
                    if (groups.size != 1) {
                        IconButton(
                            onClick = {
                                if (groups.size == 2) {
                                    groups[0] = IntervalGroup(0, Int.MAX_VALUE, 0)
                                }
                                groups.removeAt(groups.lastIndex)
                            },
                            content = {
                                Icon(
                                    tint = colorScheme.error,
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Group"
                                )
                            }
                        )
                    }

                    IconButton(
                        onClick = {
                            val last = groups.last().end
                            if (last != 0) {
                                val lastIdx = groups.lastIndex
                                groups[lastIdx] = groups[lastIdx].copy(end = (lastIdx + 1) * 5)
                                groups.add(IntervalGroup((lastIdx + 1) * 5 + 1, Int.MAX_VALUE, 0))
                            } else {
                                groups.add(IntervalGroup(0, Int.MAX_VALUE, 0))
                            }
                        },
                        content = {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Group")
                        }
                    )
                }


            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /*item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Start",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                        Text(
                            text = "End",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding()
                        )
                        Text(
                            text = "Interval",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }
                }*/

                itemsIndexed(groups) { idx, group ->

                    Text(
                        text = "Group: ${idx + 1}",
                        modifier = Modifier.padding(vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )

                    IntervalGroupRow(
                        startIdx = group.start,
                        endIdx = group.end,
                        interval = group.interval,
                        isFirstIndex = idx == 0,
                        isLastIndex = idx == groups.lastIndex,
                        onStartValueChange = {
                            val num = it.toIntOrNull()
                            val currentGroup = groups[idx]
                            groups[idx] = currentGroup.copy(start = num)
                            val previousGroup = groups[idx - 1]
                            if (num != null) {
                                groups[idx - 1] = previousGroup.copy(end = num - 1)
                            }

                        },
                        onEndValueChange = {
                            val num = it.toIntOrNull()
                            val currentGroup = groups[idx]
                            groups[idx] = currentGroup.copy(end = num)
                            val nextGroup = groups[idx + 1]
                            if (num != null) {
                                groups[idx + 1] = nextGroup.copy(start = num + 1)
                            }
                        },
                        onIntervalChange = { s, m ->
                            val second = s?.times(1000) ?: 0
                            val milliSecond = m ?: 0
                            val currentGroup = groups[idx]
                            groups[idx] = currentGroup.copy(interval = second + milliSecond)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 2.dp),
                        color = colorScheme.onSurface
                    )
                }
            }

        },
        confirmButton = {
            TextButton(
                onClick = {
                    groups.forEachIndexed { idx, it ->
                        if (it.start == null || it.end == null) {
                            Toast.makeText(
                                context,
                                "Set Range at row no: ${idx + 1}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@TextButton
                        } else if (it.start >= it.end || it.interval == null) {
                            Toast.makeText(
                                context,
                                if (it.interval == null) {
                                    "Set interval at row no: ${idx + 1}"
                                } else "Start must be Less Than End",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@TextButton
                        }

                        Log.d("izaz", "$it")

                    }
                    onSave(groups)
                }
            ) {
                Text(
                    text = "Save",
                    color = colorScheme.primary,
                    style = typography.labelLarge
                )
            }
        },
        dismissButton = {

            TextButton(onClick = { onDismiss() }) {
                Text(
                    text = "Cancel",
                    color = colorScheme.error,
                    style = typography.labelLarge
                )
            }

        },
        onDismissRequest = {  },
        tonalElevation = 6.dp

    )
}


//@Preview(showBackground = true)
@Composable
fun IntervalGroupRow(
    modifier: Modifier = Modifier,
    startIdx: Int? = 0,
    endIdx: Int? = 6,
    interval: Long? = 0L,
    isFirstIndex: Boolean = true,
    isLastIndex: Boolean = true,
    onStartValueChange: (String) -> Unit = {},
    onEndValueChange: (String) -> Unit = {},
    onIntervalChange: (Long?, Long?) -> Unit
) {


    val sec by remember(interval) { mutableStateOf((interval?.div(1000))) }
    val mili by remember(interval) { mutableStateOf((interval?.mod(1000))) }


    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        item {
//            if (isFirstIndex) {
//                Text(
//                    modifier = Modifier.padding(start = 12.dp, bottom = 20.dp),
//                    text = "0",
//                    textAlign = TextAlign.Center,
//                    style = typography.labelLarge
//                )
//            } else {
            OutlinedTextField(
                modifier = Modifier.width(70.dp),
                readOnly = isFirstIndex,
                value = startIdx?.toString() ?: "",
                supportingText = { Text("Start") },
                isError = startIdx == null || endIdx == null || startIdx >= endIdx,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { onStartValueChange(it) }
            )
//            }
        }

        item {
            Text(
                modifier = Modifier.padding(start = 2.dp, bottom = 20.dp),
                textAlign = TextAlign.Center,
                text = "To",
                style = typography.labelLarge
            )
        }

        item {
//            if (isLastIndex) {
//                Text(
//                    modifier = Modifier.padding(start = 2.dp, bottom = 20.dp),
//                    textAlign = TextAlign.Center,
//                    text = "Last",
//                    style = typography.labelLarge
//                )
//            } else {
            OutlinedTextField(
                modifier = Modifier
                    .width(70.dp),
                value = if (isLastIndex) {
                    "Last"
                } else {
                    endIdx?.toString() ?: ""
                },
                readOnly = isLastIndex,
                isError = startIdx == null || endIdx == null || startIdx >= endIdx,
                onValueChange = { onEndValueChange(it) },
                supportingText = { Text("End") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
//            }
        }

        item {

            OutlinedTextField(
                modifier = Modifier.width(80.dp),
                supportingText = { Text("Second", fontSize = 11.sp) },
                placeholder = { Text("0") },
                textStyle = TextStyle(fontSize = 12.sp),
                value = if (sec == 0L) {
                    ""
                } else sec.toString(),
                onValueChange = {
                    onIntervalChange(it.toLongOrNull(), mili?.toLong())
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

        }

        item {

            OutlinedTextField(
                modifier = Modifier.width(80.dp),
                supportingText = { Text("Mili", fontSize = 11.sp) },
                placeholder = { Text("0") },
                textStyle = TextStyle(fontSize = 12.sp),
                value = if (mili == 0) {
                    ""
                } else mili.toString(),
                onValueChange = {
                    onIntervalChange(sec, it.toLongOrNull())
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )


        }

    }

}


enum class IntervalType {
    MS, SEC
}



























