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

    fun saveUserToken(token: String) {
        viewModelScope.launch {
            pref.saveUserToken(token)
        }
    }

    fun getUserName(): LiveData<String?> {
        return pref.getUserName().asLiveData()
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            pref.saveUserName(name)
        }
    }

    fun getUserId(): LiveData<String?> {
        return pref.getUserId().asLiveData()
    }

    fun saveUserId(id: String) {
        viewModelScope.launch {
            pref.saveUserId(id)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            pref.deleteAllData()
        }
    }
}