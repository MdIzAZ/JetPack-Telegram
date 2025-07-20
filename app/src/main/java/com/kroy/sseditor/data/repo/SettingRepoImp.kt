package com.kroy.sseditor.data.repo

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kroy.sseditor.domain.models.OSType
import com.kroy.sseditor.domain.models.ThemeMode
import com.kroy.sseditor.domain.repo.SettingsRepo
import com.kroy.sseditor.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingRepoImp @Inject constructor(
    private val pref: DataStore<Preferences>
) : SettingsRepo {

    companion object {
        private val THEME_KEY = stringPreferencesKey(Constants.THEME_KEY)
        private val OS_KEY = stringPreferencesKey(Constants.OS_KEY)
    }


    override suspend fun saveThemePref(colorScheme: ThemeMode) {

        pref.edit {
            it[THEME_KEY] = colorScheme.name
        }

    }

    override fun getCurrentTheme(): Flow<ThemeMode> {
        return pref.data.map {
            val theme = it[THEME_KEY] ?: ThemeMode.SYSTEM.name
            ThemeMode.valueOf(theme)
        }
    }

    override suspend fun saveOsTypePref(osType: OSType) {
        pref.edit {
            it[OS_KEY] = osType.name
        }
    }

    override fun getCurrentOsType(): Flow<OSType> {
        return pref.data.map {
            val os = it[OS_KEY] ?: OSType.Android.name
            OSType.valueOf(os)
        }
    }
}