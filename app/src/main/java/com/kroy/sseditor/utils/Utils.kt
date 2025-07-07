package com.kroy.sseditor.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import com.kroy.sseditor.models.ChatItem
import com.kroy.sseditor.models.ChatMessage
import com.kroy.sseditor.screens.ChatScreen
import com.kroy.sseditor.screens.CustomTelegramLayout
import com.kroy.sseditor.viewmodels.ContactViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

object Utils {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun CaptureAndSaveComposable(
        contactName: String,
        contactPic: Bitmap?,
        messages: List<ChatMessage>,
        initialTimeString: String, // Time in "hh:mm a" format, e.g., "12:48 AM"
        backgroundBitmap: Bitmap?,
        senderImage: Bitmap?,
        userReplySticker: Bitmap?,
        contactViewModel: ContactViewModel
    ) {
        val context = LocalContext.current
        Log.d("time set->", "utils $initialTimeString")


        LaunchedEffect(Unit) {

            Log.d("time set->", " LE $initialTimeString")
            captureAndSaveComposableToBitmap(
                context = context,
                contactName = contactName,
                contactPic = contactPic,
                messages = messages,
                initialTimeString = initialTimeString,
                backgroundBitmap = backgroundBitmap,
                senderImage = senderImage,
                userReplySticker = userReplySticker
            )
            contactViewModel.setLoading(false)
            contactViewModel.resetContactState()
        }

        // Optionally, show some UI while waiting for the capture
        // Text(text = "Capturing Composable in 5 seconds...")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun generateNewChatScreen(
        clientName: String,
        chatList: List<ChatItem>,
        r1clickedCount: Int,
        r2clickedCount: Int,
        contactViewModel: ContactViewModel
    ) {
        val context = LocalContext.current



        LaunchedEffect(Unit) {


            generateChatScreenComposableToBitmap(
                context = context,
                r2clickedCount = r2clickedCount,
                r1clickedCount = r1clickedCount,
                clientName = clientName,
                chatList = chatList
            )
            contactViewModel.setLoading(false)
            contactViewModel.resetContactState()
        }

        // Optionally, show some UI while waiting for the capture
        // Text(text = "Capturing Composable in 5 seconds...")
    }

    fun getYesterdaysDateFormatted(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1) // Subtract one day to get yesterday's date

        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault()) // Change to "dd/MM"
        return dateFormat.format(calendar.time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateChatScreenComposableToBitmap(
        context: Context,
        r2clickedCount: Int,
        r1clickedCount: Int,
        clientName: String,
        chatList: List<ChatItem>
    ) {
        // Ensure context is an Activity
        val activity = context as? Activity ?: return

        val composeView = ComposeView(context).apply {
            setContent {
                ChatScreen(
                    clientName = clientName,
                    chats = chatList,
                    r2clickedCount = r2clickedCount,
                    r1clickedCount = r1clickedCount,
                )
            }
        }

        // Attach ComposeView to the window so it can be rendered
        val parentView = activity.findViewById<ViewGroup>(android.R.id.content)
        parentView?.addView(composeView)

        // Define the target resolution
        val targetWidth = 1290 // iPhone 15 Pro Max width in pixels
        val targetHeight = 2796 // iPhone 15 Pro Max height in pixels

        // Wait for the ComposeView to be attached to the window and laid out
        composeView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (composeView.isAttachedToWindow) {
                    composeView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // Create a bitmap with the target resolution
                    val bitmap =
                        Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)

                    // Scale the canvas to fit the content correctly if needed
                    val scaleX = targetWidth.toFloat() / composeView.width
                    val scaleY = targetHeight.toFloat() / composeView.height
                    canvas.scale(scaleX, scaleY)

                    // Draw the ComposeView on the canvas
                    composeView.draw(canvas)

                    // Save the bitmap to gallery
                    saveBitmapToGallery(context, bitmap)

                    // Clean up: remove the view from its parent once done
                    parentView?.removeView(composeView)
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun captureAndSaveComposableToBitmap(
        context: Context,
        contactName: String,
        contactPic: Bitmap?,
        messages: List<ChatMessage>,
        initialTimeString: String, // Time in "hh:mm a" format, e.g., "12:48 AM"
        backgroundBitmap: Bitmap?,
        senderImage: Bitmap?,
        userReplySticker: Bitmap?
    ) {
        // Ensure context is an Activity
        val activity = context as? Activity ?: return

        val composeView = ComposeView(context).apply {
            setContent {
                CustomTelegramLayout(
                    contactName = contactName,
                    contactPic = contactPic,
                    messages = messages,
                    initialTimeString = initialTimeString,
                    backgroundBitmap = backgroundBitmap,
                    senderImage = senderImage,
                    userReplySticker = userReplySticker
                )
            }
        }

        // Attach ComposeView to the window so it can be rendered
        val parentView = activity.findViewById<ViewGroup>(android.R.id.content)
        parentView?.addView(composeView)

        // Define the target resolution
        val targetWidth = 1290 // iPhone 15 Pro Max width in pixels
        val targetHeight = 2796 // iPhone 15 Pro Max height in pixels

        // Wait for the ComposeView to be attached to the window and laid out
        composeView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (composeView.isAttachedToWindow) {
                    composeView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // Create a bitmap with the target resolution
                    val bitmap =
                        Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)

                    // Scale the canvas to fit the content correctly if needed
                    val scaleX = targetWidth.toFloat() / composeView.width
                    val scaleY = targetHeight.toFloat() / composeView.height
                    canvas.scale(scaleX, scaleY)

                    // Draw the ComposeView on the canvas
                    composeView.draw(canvas)

                    // Save the bitmap to gallery
                    saveBitmapToGallery(context, bitmap)

                    // Clean up: remove the view from its parent once done
                    parentView?.removeView(composeView)
                }
            }
        })
    }

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore for Android 10 and above
            val contentValues = ContentValues().apply {
                put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    "saved_image_${System.currentTimeMillis()}.png"
                )
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            uri?.let {
                context.contentResolver.openOutputStream(it).use { outputStream ->
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    }
                }
                Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
            } ?: Toast.makeText(context, "Error saving image", Toast.LENGTH_SHORT).show()
        } else {
            // Save directly for older Android versions
            val picturesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File(picturesDir, "saved_image_${System.currentTimeMillis()}.png")

            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error saving image: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun getBitmapFromResource(context: Context, resourceId: Int): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, resourceId)
    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Function to convert a drawable resource to Base64 string
    fun drawableToBase64(context: Context, drawableResId: Int): String {
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseTimeString(timeString: String): LocalTime {
        return try {
            // Use Locale to avoid issues with AM/PM formatting in different regions
            val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            LocalTime.parse(timeString.trim(), formatter)
        } catch (e: DateTimeParseException) {
            Log.d("time set->", "utils entered error ")
            // Handle parsing error by returning a default time, e.g., midnight
            LocalTime.MIDNIGHT
        }
    }

    fun removeLeadingZero(time: String): String {
        // Split the input string by ":"
        val parts = time.split(":")
        if (parts.size != 2) {
            throw IllegalArgumentException("Invalid time format. Expected format: HH:mm")
        }

        // Remove leading zero from the hour part
        val hour = parts[0].toInt()
            .toString() // This converts the hour to an integer and back to a string, removing leading zero
        val minute = parts[1] // Keep the minute part unchanged

        // Return the formatted time
        return "$hour:$minute"
    }


    // Function to generate a random time between a given range (in minutes)
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateRandomTime(baseTime: LocalTime, minMinutes: Int, maxMinutes: Int): LocalTime {
        val randomMinutes = Random.nextInt(minMinutes, maxMinutes + 1)
        return baseTime.plusMinutes(randomMinutes.toLong())
    }

    fun convertLettersToUppercase(input: String): String {
        return input.map {
            if (it.isLetter()) it.uppercaseChar() else it
        }.joinToString("")
    }

    fun getBatteryImage(dayName: String): Int {
        Log.d("day entered ->", "$dayName")
        val index = com.kroy.ssediotor.R.drawable.battery80
        val list = listOf(
            com.kroy.ssediotor.R.drawable.battery80,
            com.kroy.ssediotor.R.drawable.battery50,
            com.kroy.ssediotor.R.drawable.battery90,
            com.kroy.ssediotor.R.drawable.battery25,
            com.kroy.ssediotor.R.drawable.battery70,
            com.kroy.ssediotor.R.drawable.battery35,
            com.kroy.ssediotor.R.drawable.battery60,
        )
        when (dayName) {
            "Day 1" -> return list[0]
            "Day 2" -> return list[3]
            "Day 3" -> return list[1]
            "Day 4" -> return list[5]
            "Day 5" -> return list[2]
            "Day 6" -> return list[4]
            "Day 7" -> return list[6]
        }

        return index
    }

    fun getTotalUnreadMessages(dayName: String): String {
        Log.d("day entered ->", "$dayName")
        val index = "3.9K"
        val list = listOf(
            listOf("3.9K", "4.1K", "4.2K"), // day 1
            listOf("2.5K", "2.7K", "2.8K"), // day 2
            listOf("3.1K", "3.2K", "3.4K"), // day 3
            listOf("1.9K", "2.1K", "4.1K"), // day 4
            listOf("1.8K", "1.9K", "2.1K"), // day 5
            listOf("2.1K", "2.2K", "2.3K"), // day 6
            listOf("4.9K", "5.1K", "5.2K"), // day 7

        )
        when (dayName) {
            "Day 1" -> return list[0][Random.nextInt(0, 3)]
            "Day 2" -> return list[1][Random.nextInt(0, 3)]
            "Day 3" -> return list[2][Random.nextInt(0, 3)]
            "Day 4" -> return list[3][Random.nextInt(0, 3)]
            "Day 5" -> return list[4][Random.nextInt(0, 3)]
            "Day 6" -> return list[5][Random.nextInt(0, 3)]
            "Day 7" -> return list[6][Random.nextInt(0, 3)]
        }

        return index
    }
}