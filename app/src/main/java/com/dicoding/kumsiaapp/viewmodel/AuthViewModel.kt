package com.dicoding.kumsiaapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kumsiaapp.data.remote.request.OrganizationRegisterDTO
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

class AuthViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerMessage = MutableLiveData<com.dicoding.kumsiaapp.utils.Event<String>>()
    val registerMessage : LiveData<com.dicoding.kumsiaapp.utils.Event<String>> = _registerMessage

    fun registerForOrganization(registerDTO: OrganizationRegisterDTO) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().registerForOrganization(registerDTO)
        client.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerMessage.value = com.dicoding.kumsiaapp.utils.Event("Account is successfully registered")
                } else {
                    _registerMessage.value = com.dicoding.kumsiaapp.utils.Event("Email or username has been used")
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("error apa cuy", "onFailure: ${t.message.toString()}")
                _isLoading.value = false
                _registerMessage.value = com.dicoding.kumsiaapp.utils.Event("Something is wrong.")
            }
        })
    }
}