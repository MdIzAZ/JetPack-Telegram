package com.kroy.sseditor.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kroy.ssediotor.R
import com.kroy.sseditor.models.ApiResponse
import com.kroy.sseditor.models.editContactBody
import com.kroy.sseditor.ui.theme.CustomRegularTypography
import com.kroy.sseditor.ui.theme.Primary
import com.kroy.sseditor.utils.SelectedContact
import com.kroy.sseditor.utils.Utils.base64ToBitmap
import com.kroy.sseditor.utils.Utils.drawableToBase64
import com.kroy.sseditor.viewmodels.EditContactViewModel

import java.io.ByteArrayOutputStream
import java.io.PipedReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactScreen(
    onSaveClicked: (Int) -> Unit
) {
    val context = LocalContext.current
    val editContactViewModel: EditContactViewModel = hiltViewModel()
    var hasNavigated by remember { mutableStateOf(false) } // Track if navigation has occurred


    LaunchedEffect(key1 = Unit) {
        editContactViewModel.getcontactDetails(SelectedContact.contactId, context)
    }

    val contactDetails: State<ApiResponse.ContactDetailsResponse?> =
        editContactViewModel.filteredgetContactDetailsResponse.collectAsState()
    val editcontact: State<ApiResponse.EditContacttResponse?> =
        editContactViewModel.filterededitContactResponse.collectAsState()

    if (editcontact.value?.data != null && !hasNavigated) {
        hasNavigated = true
        onSaveClicked(SelectedContact.contactId)
        Toast.makeText(context, "Contact Updated", Toast.LENGTH_SHORT).show()
    }
    if (editcontact.value?.data == null && !hasNavigated && editcontact?.value?.message != null) {
        hasNavigated = true
        onSaveClicked(SelectedContact.contactId)
        Toast.makeText(context, editcontact?.value?.message, Toast.LENGTH_SHORT).show()
    }


    var detailsfetched by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBackgroundUri by remember { mutableStateOf<Uri?>(null) }
    var imageBase64 by remember { mutableStateOf("") }
    var backgroundImageBase64 by remember { mutableStateOf("") }
    var comment1State by remember { mutableStateOf("") }
    var comment2State by remember { mutableStateOf("") }
    var comment3State by remember { mutableStateOf("") }

    // Load default empty bitmaps to prevent null pointer exceptions
    val defaultBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_pic)
    var clientImageBitmap by remember { mutableStateOf(defaultBitmap) }
    var backgroundImageBitmap by remember { mutableStateOf(defaultBitmap) }

    if (contactDetails.value?.data != null && !detailsfetched) {
        // Access the contact details data
        comment1State = contactDetails.value?.data!!.comment1
        comment2State = contactDetails.value?.data!!.comment2
        comment3State = contactDetails.value?.data!!.comment3
        imageBase64 = contactDetails.value?.data!!.contactImage
        backgroundImageBase64 = contactDetails.value?.data!!.uploadedImage

        // Decode images from base64 to bitmap if available, else use default
        clientImageBitmap = base64ToBitmap(imageBase64) ?: defaultBitmap
        backgroundImageBitmap = base64ToBitmap(backgroundImageBase64) ?: defaultBitmap

        detailsfetched = true // Ensure this is only done once
    }

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
            text = "Edit Contact",
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
            text = "Name : ${SelectedContact.contactName}", // Assuming clientItem has a clientName field
            fontSize = 24.sp,
            style = CustomRegularTypography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = Primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Image Picker for Contact Image
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
            Text("Upload Profile Photo", Modifier.padding(8.dp), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            bitmap = selectedImageUri?.let {
                BitmapFactory.decodeStream(context.contentResolver.openInputStream(it))
                    .asImageBitmap()
            } ?: clientImageBitmap.asImageBitmap(),
            contentDescription = "Contact Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f)
                .weight(1f)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Image Picker for Background Image
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
            Text("Upload Image", Modifier.padding(8.dp), fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            bitmap = selectedBackgroundUri?.let {
                BitmapFactory.decodeStream(context.contentResolver.openInputStream(it))
                    .asImageBitmap()
            } ?: backgroundImageBitmap.asImageBitmap(),
            contentDescription = "Background Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.9f)
                .weight(1f)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        // Comment Fields
        OutlinedTextField(
            value = comment1State,
            onValueChange = { if (it.length <= 60) comment1State = it },
            label = { Text("Comment 1") },

            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                cursorColor = Primary,
                focusedIndicatorColor = Primary,
                unfocusedIndicatorColor = Primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = comment2State,
            onValueChange = { if (it.length <= 60) comment2State = it },
            label = { Text("Comment 2") },
            enabled = comment1State.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                cursorColor = Primary,
                focusedIndicatorColor = Primary,
                unfocusedIndicatorColor = Primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = comment3State,
            onValueChange = { if (it.length <= 60) comment3State = it },
            label = { Text("Comment 3") },
            enabled = comment1State.isNotEmpty() && comment2State.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldDefaults.colors(
                cursorColor = Primary,
                focusedIndicatorColor = Primary,
                unfocusedIndicatorColor = Primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save button with validation
        Button(
            onClick = {
                if (backgroundImageBase64.isNotEmpty()) {
                    editContactViewModel.editContact(
                        contactId = SelectedContact.contactId,
                        editContactBody = editContactBody(
                            contactName = SelectedContact.contactName,
                            contactImage = imageBase64,
                            comment1 = comment1State,
                            comment2 = comment2State,
                            comment3 = comment3State,
                            uploadedImage = backgroundImageBase64
                        ),
                        context
                    )
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
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
fun PreviewEditContactScreen() {
    val context = LocalContext.current
    EditContactScreen(
    ) {

    }
}
