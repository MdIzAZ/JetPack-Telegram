package com.kroy.sseditor.domain.repo

import com.kroy.sseditor.domain.models.OSType
import com.kroy.sseditor.domain.models.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepo {

    suspend fun saveThemePref(colorScheme: ThemeMode)

    fun getCurrentTheme(): Flow<ThemeMode>

    suspend fun saveOsTypePref(osType: OSType)

    fun getCurrentOsType(): Flow<OSType>

}