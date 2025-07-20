package com.kroy.sseditor.presentation.navigation

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kroy.sseditor.domain.models.OSType
import com.kroy.sseditor.presentation.SharedViewModel
import com.kroy.sseditor.presentation.add_client.AddClientScreen
import com.kroy.sseditor.presentation.chat.ChatScreenContent
import com.kroy.sseditor.presentation.client.ClientScreen
import com.kroy.sseditor.presentation.client.ClientViewModel
import com.kroy.sseditor.presentation.contact_list.ContactListScreenContent
import com.kroy.sseditor.presentation.login.LoginScreen
import com.kroy.sseditor.presentation.sevenday.SelectTimeViewModel
import com.kroy.sseditor.presentation.sevenday.SevenDayScreen
import com.kroy.sseditor.presentation.splash.SplashScreen
import com.kroy.sseditor.utils.DataStoreHelper
import com.kroy.sseditor.utils.SelectedClient
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    modifier: Modifier,
    currentOSType: OSType,
    dataStoreHelper: DataStoreHelper,
    selectTimeViewModel: SelectTimeViewModel,
    sharedViewModel: SharedViewModel
) {

    val isLoggedIn by dataStoreHelper.isLoggedInFlow.collectAsState(initial = false)

    val userId by dataStoreHelper.userIdFlow.collectAsState(initial = 0)
    var hasNavigated by rememberSaveable { mutableStateOf(false) }

    val navController = rememberNavController()


    LaunchedEffect(isLoggedIn) {
        if (!hasNavigated) {
            delay(1000)
            if (isLoggedIn) {
                navController.navigate("client/${userId}") {
                    popUpTo(0)
                }
            } else {
                navController.navigate("login") {
                    popUpTo(0)
                }
            }
            hasNavigated = true
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "splash",
    ) {

        composable(route = "splash") {
            SplashScreen()
        }

        composable(route = "login") {
            LoginScreen() { loggedInUserId ->
                navController.navigate("client/$loggedInUserId") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }




        composable(
            route = "client/{userId}",
            arguments = listOf(navArgument(name = "userId") { type = NavType.IntType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
        ) {

            val clientViewModel = hiltViewModel<ClientViewModel>()
            val state by clientViewModel.clientScreenState.collectAsStateWithLifecycle()


            ClientScreen(
                state = state,
                currentOSType = currentOSType,
                onAddClient = {
                    navController.navigate("addclient")
                },
                onClientClick = { clientItem ->
                    SelectedClient.clientId = clientItem.clientId
                    SelectedClient.clientImage = clientItem.clientImage
                    SelectedClient.clientName = clientItem.clientName
                    SelectedClient.backgroundImage = clientItem.backgroundImage

                    Log.d("izaz", "Client Name : ${clientItem.clientName}")


                    navController.navigate("timer/${clientItem.clientName}/${clientItem.clientId}")
                },
                onEditClick = { clientItem ->
//                    SelectedClient.clientId = clientItem.clientId
//                    SelectedClient.clientImage = clientItem.clientImage
//                    SelectedClient.clientName = clientItem.clientName
//                    SelectedClient.backgroundImage = clientItem.backgroundImage
//
//                    navController.navigate("editclient")
                },
                setClientsOnSharedViewModel = {
                    sharedViewModel.setClients(it)
                },
                onThemeSelected = sharedViewModel::saveThemeMode,
                onCheckedChange = sharedViewModel::changeOsType,
                setFolders = selectTimeViewModel::setFolders
            )
        }




        composable(
            route = "addclient",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            AddClientScreen(
                onClientAdded = {
                    navController.navigate("client/$it") {
                        popUpTo("addclient") { inclusive = true }
                    }

                }
            )
        }


        /*
        composable(
            route = "editclient",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            EditClientScreen(
                client = Client(
                    clientName = SelectedClient.clientName,
                    clientImage = SelectedClient.clientImage,
                    clientId = SelectedClient.clientId,
                    backgroundImage = SelectedClient.backgroundImage
                )
            ) {
                Log.d("Client id ->", "client id received after editing = $it")
                // Navigate back to client screen and remove 'editclient' from backstack
                navController.navigate("client/$it") {
                    popUpTo("editclient") { inclusive = true }
                }
            }

        }
         */

        composable(
            route = "timer/{name}/{id}",
            arguments = listOf(
                navArgument(name = "name") { type = NavType.StringType },
                navArgument(name = "id") { type = NavType.IntType },
            ),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }

        ) {

            val state by selectTimeViewModel.sevenDayScreenState.collectAsStateWithLifecycle()
            val name = it.arguments?.getString("name") ?: "Client"
            val id = it.arguments?.getInt("id") ?: 0

            LaunchedEffect(Unit) {
                selectTimeViewModel.setClientDetails(name, id)
            }

            LaunchedEffect(Unit) {
                selectTimeViewModel.saveJsonToPublicDownloads()
            }


            SevenDayScreen(
                state = state,
                currentOSType = currentOSType,
                updateTriggerTime = selectTimeViewModel::updateTriggerTime,
                updateUiTime = selectTimeViewModel::updateUiTime,
                onGoClicked = { clientTimes, dayName ->

                    SelectedClient.dayName = dayName
                    SelectedClient.clientTimes = clientTimes

                    val (triggerTime, uiTime, interval) = clientTimes
                    val encodedTriggerTime = Uri.encode(triggerTime)
                    val encodedUiTime = Uri.encode(uiTime)

                    selectTimeViewModel.loadDummyContactItems {
                        navController.navigate("contact/$encodedTriggerTime/$encodedUiTime/$interval") {}
                    }

                },
                onThemeSelected = sharedViewModel::saveThemeMode,
                onCheckedChange = sharedViewModel::changeOsType,
                onSaveIntervals = selectTimeViewModel::setIntervals,
                setFolders = selectTimeViewModel::setFolders
            )
        }

        composable(
            route = "contact/{triggerTime}/{uiTime}/{interval}",
            arguments = listOf(
                navArgument(name = "triggerTime") { type = NavType.StringType },
                navArgument(name = "uiTime") { type = NavType.StringType },
                navArgument(name = "interval") { type = NavType.IntType }
            ),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {


            val state by selectTimeViewModel.contactListScreenState.collectAsStateWithLifecycle()

            val triggerTimeString = it.arguments?.getString("triggerTime") ?: "08:00"
            val uiTimeString = it.arguments?.getString("uiTime") ?: "08:00"
            val interval = it.arguments?.getInt("interval") ?: 5

            val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

            val triggerTime = remember(triggerTimeString) {
                LocalTime.parse(triggerTimeString, formatter)
            }


            ContactListScreenContent(
                osType = currentOSType,
                state = state,
                startShowingContacts = {
                    selectTimeViewModel.startShowingChatItemsWithDelay(
                        uiTime = uiTimeString,
                        triggerTime = triggerTime
                    )
                },
                onChatClick = {
                    navController.navigate("chat/${it}")
                },
                onLongPress = {
                    selectTimeViewModel.onLongPress()
                },
                onNavigateBack = {
                    selectTimeViewModel.clearContactScreenState()
                    navController.navigateUp()
                }
            )


        }

        composable(
            route = "chat/{id}",
            arguments = listOf(navArgument(name = "id") { type = NavType.IntType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {

            val id = it.arguments?.getInt("id") ?: 0

            LaunchedEffect(id) {
                selectTimeViewModel.fetchChatScreenDetails(id)
            }

            val state by selectTimeViewModel.chatScreenState.collectAsStateWithLifecycle()

            ChatScreenContent(
                contactId = id,  /* Although passing contact id inside state , but still pass directly other wise not able to get when sending msg*/
                osType = currentOSType,
                state = state,
                onBackClick = {
                    navController.navigateUp()
                },
                onMessageSend = selectTimeViewModel::addNewChatItem,
                editMessage = selectTimeViewModel::editTextMessage
            )
        }

        /*
        composable(route = "addcontact") {
            AddContactScreen(onContactAdded = {
                navController.navigate("contact") {
                    popUpTo("addcontact") { inclusive = true }
                }
            })
        }

        */


        /*
        composable(route = "editcontact") {
            EditContactScreen(onSaveClicked = {
                navController.navigate("contact") {
                    popUpTo("editcontact") { inclusive = true }
                }
            })
        }
        */


        /*
        composable(route = "transfercontact") {
            ContactTransferScreen(
                allContacts =
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

        */


        /*
        composable(route = "category") {
            CategoryScreen() {
                navController.navigate("detail/$it")
            }
        }

        */


        /*
        composable(
            route = "detail/{category}",
            arguments = listOf(
                navArgument(name = "category") {
                    type = NavType.StringType
                }
            )
        ) {
            DetailScreen()
        }

        */
    }
}


