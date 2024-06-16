package com.dicoding.kumsiaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kumsiaapp.data.remote.response.EventResponseDTO
import com.dicoding.kumsiaapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class EventViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventData = MutableLiveData<EventResponseDTO>()
    val eventData: LiveData<EventResponseDTO> = _eventData

    fun getAllEvents(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllOrganizationEvents(token)
        client.enqueue(object : retrofit2.Callback<EventResponseDTO> {
            override fun onResponse(
                call: Call<EventResponseDTO>,
                response: Response<EventResponseDTO>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _eventData.value = response.body()
                }
            }
            override fun onFailure(call: Call<EventResponseDTO>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}