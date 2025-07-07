package com.kroy.sseditor.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kroy.ssediotor.R
import com.kroy.sseditor.models.ApiResponse
import com.kroy.sseditor.models.userloginBody
import com.kroy.sseditor.ui.theme.CustomBoldTypography
import com.kroy.sseditor.ui.theme.Primary
import com.kroy.sseditor.viewmodels.UserViewModel

@Preview
@Composable
fun preview(){
    LoginScreen { userId->
        // Handle login action
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSubmit: (Int) -> Unit,

) {
    val userViewModel:UserViewModel = hiltViewModel()
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var hasSubmitted by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val userlogin: State<ApiResponse.UserLoginResponse?> = userViewModel.filteredUserLoginResponse.collectAsState()
    val isloggedIn: State<Boolean> = userViewModel.isLoggedInFlow.collectAsState(false)
    Log.d("passing id->","Logging pre ${isloggedIn.value}  ${userlogin.value} , ")
// Check if user data is available and user is not logged in
    if (userlogin.value!!.data != null && !isloggedIn.value && !hasSubmitted) {
        Log.d("passing id->", "Logging in ... ")
        val userId = userlogin.value!!.data!!.userId
        onSubmit(userId)

        // Update flags to prevent future submissions
        hasSubmitted = true
        userViewModel.setIsLoggedIn(true)
        userViewModel.setUserName(userId)
    }


    // Gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Primary
//                brush = Brush.radialGradient(
//                    colors = listOf(PrimaryLight, Primary), // Gradient colors
//                )
            )
            .padding(16.dp)
    ) {
        // Logo at the top-left corner
        Image(
            painter = painterResource(id = R.drawable.logo_circle),  // Logo image
            contentDescription = "Logo",
            modifier = Modifier
            //    .width(200.dp)
                .size(100.dp) // Adjust logo size
                .align(Alignment.TopStart),
            contentScale = ContentScale.Fit
        )

        // Box for login form
        Box(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)) // Rounded corners for the Box
                .background(color = Color.White) // Box background color
                .padding(16.dp)
                .align(Alignment.Center) // Center the login form
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Text above the User ID field
                Text(
                    text = "Login",
                    fontWeight = FontWeight.ExtraBold,
                    style = CustomBoldTypography.titleLarge, // Use custom typography
                    color = Primary,
                    fontSize = 34.sp,

                )

                // User ID Text Field
                OutlinedTextField(
                    value = userId,

                    onValueChange = { userId = it },
                    label = { Text(text = "Username", color = Primary) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                    cursorColor = Primary,
                    focusedIndicatorColor = Primary,
                    unfocusedIndicatorColor = Primary
                )
                )

                // Password Text Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Password", color = Primary) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                    cursorColor = Primary,
                    focusedIndicatorColor = Primary,
                    unfocusedIndicatorColor = Primary
                )
                )
                Spacer(modifier = Modifier

                    .height(5.dp)
                    .fillMaxWidth(),

                    )

                // Submit Button
                Button(
                    onClick = {
                        if(userId!="" && password!=""){
                            userViewModel.loginuser(
                                userloginBody(
                                    password = password,
                                    username = userId), context = context)
                        }else{
                            Toast.makeText(context, "Username or Password cannot be empty", Toast.LENGTH_SHORT).show()
                        }


                              },
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Submit", modifier = Modifier
                        .padding(8.dp))
                }
            }
        }
    }
}
