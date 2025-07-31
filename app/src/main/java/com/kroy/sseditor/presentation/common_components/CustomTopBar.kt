package com.kroy.sseditor.presentation.common_components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DriveFolderUpload
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kroy.sseditor.domain.models.OSType
import com.kroy.sseditor.domain.models.ThemeMode
import com.kroy.sseditor.presentation.contact_list.ios.components.readJsonFromUri

@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    title: String,
    currentOSType: OSType,
    isNotificationEnabled: Boolean,
    setFolders: (List<Pair<String, Int>>) -> Unit,
    onOsTypeChange: (OSType) -> Unit,
    onNotificationModeChange: (Boolean) -> Unit,
    onThemeSelected: (ThemeMode) -> Unit
) {

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val result = readJsonFromUri(context, uri)
                setFolders(result)
            }
        }
    )


    var isRotated by remember { mutableStateOf(false) }
    var isMenuOpen by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isRotated) -180f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        )
    )


    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .shadow(elevation = 8.dp)
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "  $title",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                IconButton(
                    onClick = { launcher.launch(arrayOf("application/json")) }
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        imageVector = Icons.Default.DriveFolderUpload,
                        contentDescription = "Select Folders"
                    )
                }

                OsToggleBtn(
                    currentOSType = currentOSType,
                    onCheckedChange = onOsTypeChange
                )

                Box {
                    IconButton(
                        onClick = {
                            isRotated = !isRotated
                            isMenuOpen = true
                        }
                    ) {
                        Icon(
                            modifier = Modifier.rotate(rotationAngle),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }

                    DropdownMenu(
                        expanded = isMenuOpen,
                        onDismissRequest = {
                            isMenuOpen = false
                            isRotated = false
                        }
                    ) {
                        var isThemeMenuExpanded by remember { mutableStateOf(false) }

                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text("Theme  ")

                                    Icon(imageVector = Icons.Default.Contrast, contentDescription = "Theme")

                                }
                            },
                            onClick = { isThemeMenuExpanded = true }
                        )

                        DropdownMenu(
                            expanded = isThemeMenuExpanded,
                            onDismissRequest = { isThemeMenuExpanded = false },
                            offset = DpOffset(x = 150.dp, y = 0.dp)
                        ) {
                            ThemeMode.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.label) },
                                    onClick = {
                                        onThemeSelected(it)
                                    }
                                )
                            }
                        }

                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text("Notification  ")

                                    Switch(
                                        checked = isNotificationEnabled,
                                        onCheckedChange = { onNotificationModeChange(!isNotificationEnabled) }
                                    )

                                }
                            },
                            onClick = {

                            }
                        )

                    }
                }
            }


        }

    }
}


