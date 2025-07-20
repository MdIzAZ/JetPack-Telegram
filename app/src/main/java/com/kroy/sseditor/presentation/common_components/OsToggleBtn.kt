package com.kroy.sseditor.presentation.common_components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.kroy.ssediotor.R
import com.kroy.sseditor.domain.models.OSType

@Composable
fun OsToggleBtn(
    currentOSType: OSType,
    onCheckedChange: (OSType) -> Unit
) {

    Switch(
        checked = currentOSType == OSType.IOS,
        colors = SwitchDefaults.colors(
            checkedIconColor = MaterialTheme.colorScheme.onPrimary,
            uncheckedIconColor = MaterialTheme.colorScheme.onPrimary,
            uncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            checkedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        onCheckedChange = {
            if (it) onCheckedChange(OSType.IOS)
            else onCheckedChange(OSType.Android)
        },
        thumbContent = {
            if (currentOSType == OSType.Android) {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Filled.Android,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            } else {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    painter = painterResource(R.drawable.ic_apple),
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        }
    )
}