package com.stimednp.darkmodeapp

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Created by rivaldy on Oct/28/2020.
 * Find me on my lol Github :D -> https://github.com/im-o
 */

class SettingDataStore(
    context: Context
) {
    companion object {
        private const val DATA_STORE_NAME = "setting_dark_mode.pref"
        private val IS_DARK_MODE = preferencesKey<Boolean>("is_dark_mode")
    }

    private val appContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = appContext.createDataStore(
            name = DATA_STORE_NAME
        )
    }

    suspend fun setDarkMode(uiMode: UIMode){
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = when(uiMode){
                UIMode.LIGHT -> false
                UIMode.DARK -> true
            }
        }
    }

    val uiModeFlow: Flow<UIMode> = dataStore.data
        .catch {
            if (it is IOException){
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            when(preferences[IS_DARK_MODE] ?: false){
                true -> UIMode.DARK
                false -> UIMode.LIGHT
            }
        }
}