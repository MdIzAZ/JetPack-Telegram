package com.kroy.sseditor
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kroy.ssediotor.R
import com.kroy.sseditor.models.clientItem
import com.kroy.sseditor.screens.AddClientScreen
import com.kroy.sseditor.screens.AddContactScreen
import com.kroy.sseditor.screens.CategoryScreen
import com.kroy.sseditor.screens.ClientScreen
import com.kroy.sseditor.screens.ContactScreen
import com.kroy.sseditor.screens.ContactTransferScreen
import com.kroy.sseditor.screens.DetailScreen
import com.kroy.sseditor.screens.EditClientScreen
import com.kroy.sseditor.screens.EditContactScreen
import com.kroy.sseditor.screens.LoginScreen
import com.kroy.sseditor.screens.SevenDayScreen
import com.kroy.sseditor.screens.SplashScreen
import com.kroy.sseditor.ui.theme.SSEditorTheme
import com.kroy.sseditor.utils.DataStoreHelper
import com.kroy.sseditor.utils.Permissions
import com.kroy.sseditor.utils.SelectedClient
import com.kroy.sseditor.utils.SelectedContact
import com.kroy.sseditor.viewmodels.SharedViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :FragmentActivity() {
    @Inject
    lateinit var dataStoreHelper: DataStoreHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make the activity full screen
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemUI() // Initial call to hide the system UI
        Permissions().checkAndRequestPermissions(this,this)

        setContent {
            SSEditorTheme {
                Scaffold(
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        val context = LocalContext.current
                        val sharedViewModel: SharedViewModel = viewModel()
                       App2(dataStoreHelper,sharedViewModel)
                    }
                }
            }
        }
    }
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }

    // Ensure system UI stays hidden when regaining focus
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

}
// Function to hide system UI for full-screen mode








@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App2(dataStoreHelper: DataStoreHelper,sharedViewModel: SharedViewModel) {

    val isLoggedIn by dataStoreHelper.isLoggedInFlow.collectAsState(initial = false)
    val userId by dataStoreHelper.userIdFlow.collectAsState(initial = 0)
    Log.d(" Mainactivity->","${isLoggedIn} , ${userId}")

    val navController = rememberNavController()

    // To prevent premature navigation, wait until the login status is finalized
    LaunchedEffect(isLoggedIn) {
        delay(1000)
        if (isLoggedIn) {
            // Navigate directly to client screen if already logged in
            navController.navigate("client/${userId}") {
                popUpTo(0) // Clear the backstack so that login isn't navigable after this
            }
        } else {
            // Navigate to login screen if not logged in
            navController.navigate("login") {
                popUpTo(0) // Clear the backstack so that client screen isn't navigable
            }
        }
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable(route = "splash") {  // login screen
           SplashScreen()
        }
        composable(route = "login") {  // login screen
            LoginScreen() { loggedInUserId ->
                Log.d("passing id->", "$loggedInUserId")
                // After login, navigate to the client screen
                navController.navigate("client/$loggedInUserId") {
                    popUpTo("login") { inclusive = true }  // Clear login screen from backstack
                }
            }
        }

        composable(
            route = "client/{userId}",
            arguments = listOf(navArgument(name = "userId") { type = NavType.IntType })
        ) {
            ClientScreen(
                onAddClient = {
                    navController.navigate("addclient")
                },
                onClientClick = { clientItem ->
                    SelectedClient.clientId = clientItem.clientId
                    SelectedClient.clientImage = clientItem.clientImage
                    SelectedClient.clientName = clientItem.clientName
                    SelectedClient.backgroundImage = clientItem.backgroundImage

                    navController.navigate("timer/${clientItem.clientName}/${clientItem.clientId}")
                },
                onEditClick = { clientItem ->
                    SelectedClient.clientId = clientItem.clientId
                    SelectedClient.clientImage = clientItem.clientImage
                    SelectedClient.clientName = clientItem.clientName
                    SelectedClient.backgroundImage = clientItem.backgroundImage

                    navController.navigate("editclient")
                },
                sharedViewModel
            )
        }

        composable(route = "addclient") {
            AddClientScreen(onClientAdded = {
                navController.navigate("client/$it") {
                    popUpTo("addclient") { inclusive = true }
                }

            })
        }

        composable(route = "editclient") {
            EditClientScreen(clientItem = clientItem(
                clientName = SelectedClient.clientName,
                clientImage = SelectedClient.clientImage,
                clientId = SelectedClient.clientId,
                backgroundImage = SelectedClient.backgroundImage
            )) {
                Log.d("Client id ->", "client id received after editing = $it")
                // Navigate back to client screen and remove 'editclient' from backstack
                navController.navigate("client/$it") {
                    popUpTo("editclient") { inclusive = true }
                }
            }

        }

        composable(route = "timer/{name}/{id}",
            arguments = listOf(
                navArgument(name = "name") { type = NavType.StringType },
                navArgument(name = "id") { type = NavType.IntType },
            )
        ) {
            SevenDayScreen(onGoClicked = { time, dayName ->
                println("Received day and time $dayName at $time")
                SelectedClient.dayName = dayName
                SelectedClient.time = time
                println("Updated SelectedClient: ${SelectedClient.dayName} at ${SelectedClient.time}")
                navController.navigate("contact")
            })
        }

        composable(route = "contact") {
            ContactScreen(onAddContact = {
                navController.navigate("addcontact")
            },
                onEditClick = { contactItem ->
                    SelectedContact.contactId = contactItem.contactId
                    SelectedContact.contactName = contactItem.contactName
                    navController.navigate("editcontact")
                },
                onTransferContact = {
                    //TODO():  on transfer click go to TransferContact  page
                    navController.navigate("transfercontact")
                },
                sharedViewModel
            )
        }

        composable(route = "addcontact") {
            AddContactScreen(onContactAdded = {
                navController.navigate("contact") {
                    popUpTo("addcontact") { inclusive = true }
                }
            })
        }

        composable(route = "editcontact") {
            EditContactScreen(onSaveClicked = {
                navController.navigate("contact") {
                    popUpTo("editcontact") { inclusive = true }
                }
            })
        }
        composable(route = "transfercontact") {
            ContactTransferScreen(allContacts =
                sharedViewModel.contacts.value!!,
                allClients = sharedViewModel.clients.value!!,
                onTransferComplete = {
                    navController.navigate("contact") {
                        popUpTo("transfercontact") { inclusive = true }
                    }
                    //TODO() : on transfer click
                }
            )
        }



















        composable(route = "category") {
            CategoryScreen(){
                navController.navigate("detail/$it")
            }
        }

        composable(route = "detail/{category}",
            arguments = listOf(
                navArgument(name = "category") {
                    type = NavType.StringType
                }
            )
        ) {
            DetailScreen()
        }
    }
}


// Function to save bitmap to storage
fun saveBitmapToLocalStorage(context: Context, bitmap: Bitmap) {
    val fileName = "composable_image_${System.currentTimeMillis()}.png"
    val file = File(context.getExternalFilesDir(null), fileName)
    try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Log.d("SaveBitmap", "Image saved: ${file.absolutePath}")
    } catch (e: IOException) {
        e.printStackTrace()
        Log.e("SaveBitmap", "Error saving image: ${e.localizedMessage}")
    }
}


@Composable
fun DerivedAndProduced(){
    val tableOf = remember {
        mutableStateOf(5)
    }
    val index = produceState(initialValue = 1 ){
        repeat(9){
            delay(1000)
            value+=1
        }
    }
    val message = remember {
        derivedStateOf {
            "${tableOf.value} * ${index.value} = ${tableOf.value * index.value}"
        }
    }
    Box(
        contentAlignment =  Alignment.Center,
        modifier =  Modifier.fillMaxSize(1f)

    ){
        Text(text = message.value)
    }

}
@Preview
@Composable
fun CircularImage(){

    Image(painter = painterResource(id = R.drawable.person),
        contentDescription ="Circular Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .wrapContentSize()


            .size(80.dp)
            .clip(CircleShape)
            .border(2.dp, Color.LightGray, CircleShape)
            .padding(10.dp)
    )
}


@Composable
fun ListViewItem(imgID:Int , name:String ,profession:String){
    Row(horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(10.dp)) {
        Column (){
            Box(
                contentAlignment = Alignment.Center
            ){
                Image(painter = painterResource(id = imgID),
                    contentDescription = "bg",
                    Modifier.size(80.dp),
                    contentScale = ContentScale.Inside,


                    )
                Image(painter = painterResource(id = R.drawable.person),
                    contentDescription = "person",
                    Modifier.
                    size(40.dp)
                )
            }
        }
        Column(
            Modifier
                .padding(5.dp,10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = name,
                fontSize = 25.sp,
                fontStyle =FontStyle.Normal,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1

            )
            Text(
                text = profession,
                fontSize = 18.sp,
                fontStyle =FontStyle.Normal,
                maxLines = 1



            )
        }

    }
}


@Composable
fun Row_and_Column () {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "A",

            )
        Text(
            text = "B"
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "C",

                )
            Text(
                text = "D"
            )
        }
    }

}



@Preview(showBackground = true)
@Composable
fun PreviewFunction(){
    /** Text */
    Text(text = "Hello world",
        fontFamily = FontFamily.Cursive,
        fontSize = 20.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Red,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .wrapContentHeight()
            .border(10.dp, Color.Red)
            .padding(30.dp)
            .clickable {
                Log.d("Text click ->", "Hello!")
            }
    )
    /** Image */
//    Image(
//        painter = painterResource(id = R.drawable.ic_launcher_foreground),
//        contentDescription ="Image",
//        colorFilter = ColorFilter.tint(Color.Blue),
//        alignment = Alignment.TopCenter,
//        alpha = 0.3f,
//        contentScale = ContentScale.Inside
//
//
//        )
    /**Button*/
//    Button(onClick = {},
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.Red,
//            contentColor = Color.Black
//
//        )
//
//        ) {
//        Column {
//            Text(text = "Button",
//                textAlign = TextAlign.Center)
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                contentDescription ="Image",
//                colorFilter = ColorFilter.tint(Color.Blue),
//                alpha = 0.3f
//            )
//        }
//
//
//
//    }
//      val state = remember {
//          mutableStateOf("")
//      }
//    TextField(value = state.value,
//        onValueChange = {
//                        state.value =it
//        },
//        label = {
//            Text(text = "Enter String"
//            )
//        },
//        placeholder = {
//
//        }
//
//        )
}
@Composable
fun App(){
    var counter = remember{ mutableStateOf( 0) }
    DerivedAndProduced()
    LaunchedEffect(key1 = Unit){
//        delay(2000)
//        counter.value  = 10

    }
    //Counter2(value = counter.value)
}
@Composable
fun Counter2(value:Int){
    val state = rememberUpdatedState(newValue = value)
    LaunchedEffect(key1 = Unit){ // whenever the key value changes , then only the block gets executed
        delay (5000) // Long running task
        Log.d("Button click","Button clicked counter increased ${state.value}")
    }

    Text(text = "Value  =  ${state.value}")

}
@Composable
fun Counter(){
    var count = remember {
        mutableStateOf(0)
    }
    var key = count.value%3==0
    LaunchedEffect(key1 = key){ // whenever the key value changes , then only the block gets executed

        Log.d("Button click","Button clicked counter increased ${count.value}")
    }
    Button(onClick = { count.value++
    }) {
        Text(text = "Increment button")
    }
}