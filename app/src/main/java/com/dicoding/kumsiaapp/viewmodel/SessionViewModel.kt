package com.dicoding.kumsiaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.kumsiaapp.data.local.UserPreferences
import kotlinx.coroutines.launch

class SessionViewModel(private val pref: UserPreferences) : ViewModel() {
    fun getUserToken(): LiveData<String?> {
        return pref.getUserToken().asLiveData()
    }

    fun getUserName(): LiveData<String?> {
        return pref.getUserName().asLiveData()
    }

    fun getUserRole(): LiveData<String?> {
        return pref.getUserRole().asLiveData()
    }

    fun getIsNewUser(): LiveData<Boolean?> {
        return pref.getIsNewUser().asLiveData()
    }

    fun saveSession(token: String, name: String, role: String, isNewUser: Boolean) {
        viewModelScope.launch {
            pref.saveSession(token, name, role, isNewUser)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            pref.deleteAllData()
        }
    }
}