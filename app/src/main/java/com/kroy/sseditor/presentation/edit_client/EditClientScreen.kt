package com.kroy.sseditor.presentation.edit_client

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kroy.sseditor.data.remote.models.ApiResponse
import com.kroy.sseditor.domain.models.Client
import com.kroy.sseditor.data.remote.models.EditClientBody
import com.kroy.sseditor.presentation.theme.CustomRegularTypography
import com.kroy.sseditor.presentation.theme.Primary
import com.kroy.sseditor.utils.Utils.base64ToBitmap

import java.io.ByteArrayOutputStream


@Composable
fun EditClientScreen(
    client: Client,
    onClientEdited: (Int) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBackgroundUri by remember { mutableStateOf<Uri?>(null) }
    var imageBase64 by remember { mutableStateOf(client.clientImage) }
    var backgroundImageBase64 by remember { mutableStateOf(client.backgroundImage) }

    val context = LocalContext.current
    var hasNavigated by remember { mutableStateOf(false) } // Track if navigation has occurred
    val editClientViewModel: EditClientViewModel = hiltViewModel()
    val editClients: State<ApiResponse.AddClientResponse?> = editClientViewModel.filteredEditClientResponse.collectAsState()

    val userId: State<Int> = editClientViewModel.userIdFlow.collectAsState(0)

    if (editClients.value !=null && !hasNavigated) {
        hasNavigated = true
        Toast.makeText(context, "Client Updated", Toast.LENGTH_SHORT).show()
        onClientEdited(userId.value)
        editClientViewModel.resetClientState()
    }

    if (editClients.value?.data == null  && !hasNavigated && editClients?.value?.message!=null) {
        hasNavigated = true
        onClientEdited(userId.value)
        Toast.makeText(context, editClients?.value?.message, Toast.LENGTH_SHORT).show()
    }


    // Helper function to convert Base64 string to Bitmap
    val clientImageBitmap = remember { base64ToBitmap(client.clientImage) }
    val backgroundImageBitmap = remember { base64ToBitmap(client.backgroundImage) }

    // Activity Result API for client image picking
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            val inputStream = context.contentResolver.openInputStream(it)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            originalBitmap?.compress(Bitmap.CompressFormat.PNG, 10, outputStream)
            val compressedBytes = outputStream.toByteArray()
            imageBase64 = Base64.encodeToString(compressedBytes, Base64.DEFAULT)
        }
    }

    // Activity Result API for background image picking
    val backgroundPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedBackgroundUri = it
            val inputStream = context.contentResolver.openInputStream(it)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            originalBitmap?.compress(Bitmap.CompressFormat.PNG, 10, outputStream)
            val compressedBytes = outputStream.toByteArray()
            backgroundImageBase64 = Base64.encodeToString(compressedBytes, Base64.DEFAULT)
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Edit Client",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Display client name just below the "Edit Client" title
        Text(
            text = "Name : ${client.clientName}", // Assuming clientItem has a clientName field
            fontSize = 24.sp,
            style = CustomRegularTypography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = Primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Client Image Picker Button
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier
                .background(Color.White)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White
            )
        ) {
            Text("Select Client Image", Modifier.padding(8.dp), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show selected client image or default image
        Image(
            bitmap = selectedImageUri?.let {
                BitmapFactory.decodeStream(context.contentResolver.openInputStream(it)).asImageBitmap()
            } ?: clientImageBitmap?.asImageBitmap()!!,
            contentDescription = "Client Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f)
                .weight(1f)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Background Image Picker Button
        Button(
            onClick = { backgroundPickerLauncher.launch("image/*") },
            modifier = Modifier
                .background(Color.White)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White
            )
        ) {
            Text("Select Background Image", Modifier.padding(8.dp), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show selected background image or default image
        Image(
            bitmap = selectedBackgroundUri?.let {
                BitmapFactory.decodeStream(context.contentResolver.openInputStream(it)).asImageBitmap()
            } ?: backgroundImageBitmap?.asImageBitmap()!!,
            contentDescription = "Background Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f)
                .weight(1f)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        // Save button
        Button(
            onClick = {
                if (imageBase64.isNotEmpty() && backgroundImageBase64.isNotEmpty()) {
                    editClientViewModel.editClient(
                        editClientBody = EditClientBody(
                            clientName = client.clientName,
                            clientImage = imageBase64,
                            backgroundImage = backgroundImageBase64
                        ), clientId = client.clientId,context
                    )

                } else {
                    Toast.makeText(context, "Error Updating .. ", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .background(Color.White)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
                .fillMaxWidth(0.9f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White
            )
        ) {
            Text("Save", Modifier.padding(8.dp), fontSize = 16.sp)
        }
    }
}


@Preview
@Composable
fun PreviewEditClientScreen() {
    val context = LocalContext.current
//    EditClientScreen(
//        defaultClientImageBase64 = drawableToBase64(context, R.drawable.f),
//        defaultBackgroundImageBase64 = drawableToBase64(context,R.drawable.d)
//    )
}

