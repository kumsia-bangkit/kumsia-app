package com.dicoding.kumsiaapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUserToken(): kotlinx.coroutines.flow.Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    fun getUserName(): kotlinx.coroutines.flow.Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY]
        }
    }

    fun getUserRole(): kotlinx.coroutines.flow.Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[ROLE_KEY]
        }
    }

    fun getIsNewUser(): kotlinx.coroutines.flow.Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[IS_NEW_USER_KEY]
        }
    }

    suspend fun saveSession(token: String, name: String, role: String, isNewUser: Boolean) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[NAME_KEY] = name
            preferences[ROLE_KEY] = role
            preferences[IS_NEW_USER_KEY] = isNewUser
        }
    }

    suspend fun deleteAllData() {
        dataStore.edit { session ->
            session.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val IS_NEW_USER_KEY = booleanPreferencesKey("is_new_user")
    }
}