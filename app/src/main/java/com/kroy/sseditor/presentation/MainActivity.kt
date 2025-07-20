package com.kroy.sseditor.presentation

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kroy.sseditor.domain.models.ThemeMode
import com.kroy.sseditor.presentation.navigation.NavGraph
import com.kroy.sseditor.presentation.sevenday.SelectTimeViewModel
import com.kroy.sseditor.presentation.theme.SSEditorTheme
import com.kroy.sseditor.utils.DataStoreHelper
import com.kroy.sseditor.utils.Permissions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import android.view.WindowInsets as AndroidWindowInsets

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var dataStoreHelper: DataStoreHelper

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Permissions().checkAndRequestPermissions(this, this)
        enableEdgeToEdge()
        hideSystemUI()

        setContent {
            val sharedViewModel = hiltViewModel<SharedViewModel>()
            val selectTimeViewModel = hiltViewModel<SelectTimeViewModel>()

            val currentTheme by sharedViewModel.currentTheme.collectAsStateWithLifecycle()
            val currentOs by sharedViewModel.currentOSType.collectAsStateWithLifecycle()

            val isDarkTheme = when (currentTheme) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            SSEditorTheme(darkTheme = isDarkTheme) {

                NavGraph(
                    modifier = Modifier,
                    currentOSType = currentOs,
                    dataStoreHelper = dataStoreHelper,
                    sharedViewModel = sharedViewModel,
                    selectTimeViewModel = selectTimeViewModel
                )

            }
        }
    }


    private fun hideSystemUI() {
        // Ensure content draws behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For API 30+ (Android 11 and above)
            window.insetsController?.let { controller ->
                // Hide status and navigation bars
                controller.hide(AndroidWindowInsets.Type.statusBars() or AndroidWindowInsets.Type.navigationBars())
                // Allow bars to reappear temporarily on swipe
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // For older APIs (pre-Android 11)
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
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
}









