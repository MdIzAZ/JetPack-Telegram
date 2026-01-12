package com.kroy.sseditor.presentation.theme.sse_editor_theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.kroy.sseditor.domain.models.OSType


data class SSEditorColorScheme(

    /* ContactList Screen Top Bar */
    val editTextColor: Color,
    val topTwoIconColor: Color,
    val selectedFolderColor: Color,
    val unSelectedFolderColor: Color,
    val topbarBackgroundColor1: Color,
    val topbarBackgroundColor2: Color,
    val topbarBackgroundColor3: Color,

    //Topbar Content
    val topbarContentColor: Color,


    /* Contact Item */
    val contactItemBackgroundColor: Color,
    val onlineIndicatorColor: Color,
    val unreadCountBubbleColor: Color,

    /* Contact List Screen Bottom Bar*/
//    val bottombarBackgroundColor1: Color,
//    val bottombarBackgroundColor2: Color,
//    val bottombarBackgroundColor3: Color,

    /* Pop Up Notification */
    val popupNotificationBackgroundColor: Color,
    val popupNotificationTextColor: Color,


    /* Chat Screen Top Bar Color */
    val backBtnAndCountColor: Color,
    val lastSeenTextColor: Color,


    /* Chat Bubble */
    val senderChatBubbleColor1: Color,
    val senderChatBubbleColor2: Color,

    val receiverChatBubbleColor1: Color,
    val receiverChatBubbleColor2: Color,
    val todayTextBackgroundColor: Color,


    /* Chat Screen Bottom Bar */
    val txtFieldColor: Color,

    val txtColor: Color




)


val LocalSSEditorColorScheme = staticCompositionLocalOf { darkSSEditorColorScheme(OSType.IOS) }
