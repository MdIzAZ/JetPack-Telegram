package com.kroy.sseditor.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.kroy.ssediotor.R
import com.kroy.sseditor.models.ApiResponse
import com.kroy.sseditor.models.Client
import com.kroy.sseditor.models.addClientBody
import com.kroy.sseditor.ui.theme.CustomBoldTypography
import com.kroy.sseditor.ui.theme.CustomTypography
import com.kroy.sseditor.ui.theme.Dimens
import com.kroy.sseditor.ui.theme.Primary
import com.kroy.sseditor.viewmodels.AddClientViewModel
import com.kroy.sseditor.viewmodels.ClientViewModel
import java.io.ByteArrayOutputStream


@Preview(showBackground = true)
@Composable
fun preview3(){
  AddClientScreen(onClientAdded = {})

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClientScreen(onClientAdded: (Int) -> Unit) {
    var clientName by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBackgroundUri by remember { mutableStateOf<Uri?>(null) } // For background image
    var imageBase64 by remember { mutableStateOf("") }
    var backgroundImageBase64 by remember { mutableStateOf("") } // For background image
    val context = LocalContext.current

    val addClientViewModel: AddClientViewModel = hiltViewModel()
    val addClients: State<ApiResponse.AddClientResponse?> = addClientViewModel.filteredaddClientResponse.collectAsState()
    val userId: State<Int> = addClientViewModel.userIdFlow.collectAsState(0)
    var hasNavigated by remember { mutableStateOf(false) } // Track if navigation has occurred
    val addedClientId = addClients.value?.data?.clientId ?: 0 // Safely access the clientId, default to 0 if null

    Log.d("add client ->", "Added Client ID: $addedClientId")

    if (addedClientId != 0 && !hasNavigated) {
        hasNavigated = true // Set the flag to true after first navigation
        onClientAdded(userId.value) // Trigger navigation or further actions
        addClientViewModel.resetClientState()
    }
    if (addedClientId == 0 && !hasNavigated && addClients.value?.message!=null)  { // not able to add condition
        hasNavigated = true
        onClientAdded(userId.value)

        Toast.makeText(LocalContext.current, "${addClients.value?.message}", Toast.LENGTH_SHORT).show()
    }

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
        Text(
            text = "Add Client",
            style = CustomBoldTypography.titleMedium.copy(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            ),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = clientName,
            onValueChange = { clientName = it },
            label = { Text("Client Name", color = Primary, fontWeight = FontWeight.Normal) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                cursorColor = Primary,
                focusedIndicatorColor = Primary,
                unfocusedIndicatorColor = Primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Client Image Selection Button
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
            Text("Select Client Image", Modifier.padding(8.dp), fontSize = Dimens.ButtonText)
        }



        Spacer(modifier = Modifier.height(16.dp))

        // Show selected client image or default image
        Image(
            painter = rememberAsyncImagePainter(selectedImageUri ?: R.drawable.default_pic),
            contentDescription = "Client Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f)
                .weight(1f)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Background Image Selection Button
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
            Text("Select Background Image", Modifier.padding(8.dp), fontSize = Dimens.ButtonText)
        }

        // Show selected background image or default image
        Image(
            painter = rememberAsyncImagePainter(selectedBackgroundUri ?: R.drawable.default_pic),
            contentDescription = "Background Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f)
                .weight(1f)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        Button(
            onClick = {
                if (clientName.isNotEmpty() && imageBase64.isNotEmpty()) {
                    addClientViewModel.addClient(
                        addClientBody(
                            userId = userId.value,
                            clientImage = imageBase64,
                            clientName = clientName,
                            backgroundImage = backgroundImageBase64 // Add background image to the request
                        ), context = context
                    )
                } else {
                    Toast.makeText(context, "Error Saving .. ", Toast.LENGTH_SHORT).show()
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
            Text("Save", Modifier.padding(8.dp), fontSize = Dimens.ButtonText)
        }
    }
}
