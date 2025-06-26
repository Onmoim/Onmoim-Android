package com.onmoim.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStorePreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun putString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    fun getString(key: String): Flow<String?> = dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(key)]
    }

    suspend fun putInt(key: String, value: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }

    fun getInt(key: String): Flow<Int?> = dataStore.data.map { preferences ->
        preferences[intPreferencesKey(key)]
    }

    suspend fun removeString(key: String) {
        dataStore.edit {
            it.remove(stringPreferencesKey(key))
        }
    }

    suspend fun removeInt(key: String) {
        dataStore.edit {
            it.remove(intPreferencesKey(key))
        }
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

     fun getBoolean(key: String): Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(key)]
    }

    suspend fun removeBoolean(key: String) {
        dataStore.edit {
            it.remove(booleanPreferencesKey(key))
        }
    }
}