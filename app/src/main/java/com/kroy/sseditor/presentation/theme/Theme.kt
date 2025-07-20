package com.kroy.sseditor.presentation.theme

import android.app.Activity

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.kroy.ssediotor.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// Load your custom font
val CustomFontFamily = FontFamily(
    Font(R.font.rubit_wght),

    // Replace with your custom font file,

)

val CustomTypography = Typography(
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp
    ),
    titleMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ) ,  // Add a bold body text style


)
val CustomComfortaaFontFamily = FontFamily(
    Font(R.font.proximasoft),

    // Replace with your custom font file,

)

val CustomBoldFontFamily = FontFamily(
    Font(R.font.rubik_bold),

    // Replace with your custom font file,

)

val CustomBoldTypography = Typography(
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomBoldFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp
    ),
    titleMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomBoldFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomBoldFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomBoldFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ) ,  // Add a bold body text style


)

val CustomMediumFontFamily = FontFamily(
    Font(R.font.rubin_medium),

    // Replace with your custom font file,

)

val CustomMediumTypography = Typography(
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomMediumFontFamily,
        fontWeight = FontWeight.Thin,
        fontSize = 30.sp
    ),
    titleMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomMediumFontFamily,
        fontWeight = FontWeight.Thin,
        fontSize = 24.sp
    ),
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomMediumFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomMediumFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ) ,  // Add a bold body text style


)

val CustomRegularFontFamily = FontFamily(
    Font(R.font.rubik_regular),

    // Replace with your custom font file,

)

val CustomRegularTypography = Typography(
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomRegularFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp
    ),
    titleMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomRegularFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomRegularFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomRegularFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ) ,  // Add a bold body text style


)

val CustomSemiBoldFontFamily = FontFamily(
    Font(R.font.rubik_semibold),

    // Replace with your custom font file,

)
val CustomRobotoMediumFontFamily = FontFamily(
    Font(R.font.roboto_medium),

    // Replace with your custom font file,

)

//val CustomRegularTypography = Typography(
//    titleLarge = androidx.compose.ui.text.TextStyle(
//        fontFamily = CustomRegularFontFamily,
//        fontWeight = FontWeight.Normal,
//        fontSize = 30.sp
//    ),
//    titleMedium = androidx.compose.ui.text.TextStyle(
//        fontFamily = CustomRegularFontFamily,
//        fontWeight = FontWeight.Normal,
//        fontSize = 24.sp
//    ),
//    bodyLarge = androidx.compose.ui.text.TextStyle(
//        fontFamily = CustomRegularFontFamily,
//        fontWeight = FontWeight.Bold,
//        fontSize = 16.sp
//    ),
//    bodyMedium = androidx.compose.ui.text.TextStyle(
//        fontFamily = CustomRegularFontFamily,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp
//    ) ,  // Add a bold body text style
//
//
//)

@Composable
fun SSEditorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),

    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Primary.hashCode()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}