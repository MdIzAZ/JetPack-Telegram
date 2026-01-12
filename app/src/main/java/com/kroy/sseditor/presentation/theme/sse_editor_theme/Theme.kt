package com.kroy.sseditor.presentation.theme.sse_editor_theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.kroy.sseditor.domain.models.OSType


fun lightSSEditorColorScheme(currentOSType: OSType) :SSEditorColorScheme {

    return when(currentOSType) {
        OSType.IOS -> SSEditorColorScheme(
            editTextColor = Color(0xFF1078eb),
            topTwoIconColor = Color(0xFF2871cd),
            selectedFolderColor = Color(0xFF0a77f1),
            unSelectedFolderColor = Color(0xFF909090),
            topbarBackgroundColor1 = Color(0xFFededf7),
            topbarBackgroundColor2 = Color(0xFFf2f2f1),
            topbarBackgroundColor3 = Color(0xFFf0f1f6),
            contactItemBackgroundColor = Color.White,
            onlineIndicatorColor = Color(0xFF4CC91F),
            unreadCountBubbleColor = Color(0xFF017dfd),
            topbarContentColor = Color.Black,
            //
            popupNotificationBackgroundColor = Color.White,
            popupNotificationTextColor = Color.Black,
            //
            backBtnAndCountColor = Color(0xFF3890b6),
            lastSeenTextColor = Color(0xFF747d7c),
            senderChatBubbleColor1 = Color(0xFFe5fdfd),
            senderChatBubbleColor2 = Color(0xFFe2fffb),
            receiverChatBubbleColor1 = Color.White,
            receiverChatBubbleColor2 = Color.White,
            todayTextBackgroundColor = Color(0xFF52a4a7),
            txtFieldColor = Color.White,
            txtColor = Color.Black
        )

        OSType.Android -> SSEditorColorScheme(
            editTextColor = Color(0xFF007AFF),
            topTwoIconColor = Color(0xFF007AFF),
            //
            selectedFolderColor = Color.White,
            unSelectedFolderColor = Color(0xffd5e8f7),
            topbarBackgroundColor1 = Color(0xFF527da3),
            topbarBackgroundColor2 = Color(0xFF527da3),
            topbarBackgroundColor3 = Color(0xFF527da3),
            contactItemBackgroundColor = Color.White,
            onlineIndicatorColor = Color(0xFF4BCB1C),
            unreadCountBubbleColor = Color(0xFF4ecc5e),
            //
            popupNotificationBackgroundColor = Color.White,
            popupNotificationTextColor = Color.Black,
            //
            backBtnAndCountColor = Color.White,
            lastSeenTextColor = Color(0xFFc9deef),
            senderChatBubbleColor1 = Color(0xFFefffde),
            senderChatBubbleColor2 = Color(0xFFefffde),
            receiverChatBubbleColor1 = Color.White,
            receiverChatBubbleColor2 = Color.White,
            todayTextBackgroundColor = Color.White,
            txtFieldColor = Color.White,
            topbarContentColor = Color.Black,
            txtColor = Color.Black
        )
    }

}



fun darkSSEditorColorScheme(currentOSType: OSType): SSEditorColorScheme {
    return when (currentOSType) {
        OSType.IOS -> SSEditorColorScheme(
            editTextColor = Color(0xFF2D8EFC),
            topTwoIconColor = Color(0xFF5A92F5),
            selectedFolderColor = Color(0xFF5A92F5),
            unSelectedFolderColor = Color.Gray,
            topbarBackgroundColor1 = Color(0xFF201F24),
            topbarBackgroundColor2 = Color(0xFF252024),
            topbarBackgroundColor3 = Color(0xFF1B1A1F),
            contactItemBackgroundColor = Color.Black,
            onlineIndicatorColor = Color(0xFF4CC91F),
            unreadCountBubbleColor = Color(0xFF5A92F5),
            popupNotificationBackgroundColor = Color.Black,
            popupNotificationTextColor = Color.Gray,
            backBtnAndCountColor = Color(0xFF2D8EFC),
            lastSeenTextColor = Color(0xFFAAACAF),
            senderChatBubbleColor1 = Color(0xFFCD25EE),
            senderChatBubbleColor2 = Color(0xFF9D1EEA),
            receiverChatBubbleColor1 = Color(0xFF342525),
            receiverChatBubbleColor2 = Color(0xFF2E2133),
            todayTextBackgroundColor = Color(0xFF2E2133),
            txtFieldColor = Color.Black,
            topbarContentColor = Color.White,
            txtColor = Color.White
        )
        OSType.Android -> SSEditorColorScheme(
            editTextColor = Color(0xFF6abfff),
            topTwoIconColor = Color(0xFF6abfff),
            selectedFolderColor = Color(0xFF6abfff),
            unSelectedFolderColor = Color(0xff8b97a2),
            topbarBackgroundColor1 = Color(0xff242d39),
            topbarBackgroundColor2 = Color(0xff242d39),
            topbarBackgroundColor3 = Color(0xff242d39),
            contactItemBackgroundColor = Color(0xFF1d2733),
            onlineIndicatorColor = Color(0xFF4CC91F),
            unreadCountBubbleColor = Color(0xFF64b5ef),
            popupNotificationBackgroundColor = Color.Black,
            popupNotificationTextColor = Color.Gray,
            backBtnAndCountColor = Color.White,
            lastSeenTextColor = Color(0xff7d8e9a),
            senderChatBubbleColor1 = Color(0xFFCD25EE),
            senderChatBubbleColor2 = Color(0xFF9D1EEA),
            receiverChatBubbleColor1 = Color(0xff232e3b),
            receiverChatBubbleColor2 = Color(0xff232e3b),
            todayTextBackgroundColor = Color(0xff242f3c),
            txtFieldColor = Color(0xff212d3b),
            topbarContentColor = Color.White,
            txtColor = Color.White
        )
    }
}






@Composable
fun TelegramCustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    currentOSType: OSType,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkSSEditorColorScheme(currentOSType) else lightSSEditorColorScheme(currentOSType)

    CompositionLocalProvider(
        LocalSSEditorColorScheme provides colorScheme
    ) {
        content()
    }
}


object TelegramCustomTheme {
    val colorScheme: SSEditorColorScheme
        @Composable
        get() = LocalSSEditorColorScheme.current
}





















