package com.kroy.sseditor.presentation.client

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kroy.ssediotor.R
import com.kroy.sseditor.domain.models.Client
import com.kroy.sseditor.domain.models.OSType
import com.kroy.sseditor.domain.models.ThemeMode
import com.kroy.sseditor.presentation.client.components.ClientList
import com.kroy.sseditor.presentation.common_components.CustomTopBar

@Composable
fun ClientScreen(
    state: ClientScreenState,
    currentOSType: OSType,
    isNotificationEnabled: Boolean,
    onAddClient: () -> Unit,
    onClientClick: (Client) -> Unit,
    onEditClick: (Client) -> Unit,
    setClientsOnSharedViewModel: (List<Client>) -> Unit,
    setFolders: (List<Pair<String, Int>>) -> Unit,
    onThemeSelected: (ThemeMode) -> Unit,
    onOSTypeChange: (OSType) -> Unit,
    onNotificationModeChange: (Boolean) -> Unit,
) {


    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            CustomTopBar(
                title = "Clients",
                currentOSType = currentOSType,
                isNotificationEnabled = isNotificationEnabled,
                setFolders = setFolders,
                onThemeSelected = onThemeSelected,
                onOsTypeChange = onOSTypeChange,
                onNotificationModeChange = onNotificationModeChange
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClient,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add Client",
                )
            }
        }
    ) { ip ->

        Box(
            modifier = Modifier
                .padding(ip)
                .fillMaxSize()
        ) {

            Column(modifier = Modifier.fillMaxSize()) {
                setClientsOnSharedViewModel(state.clients)
                ClientList(
                    clients = state.clients,
                    onClick = onClientClick,
                    onEditClick = onEditClick
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                )
            }

        }
    }


}