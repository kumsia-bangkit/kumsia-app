package com.dicoding.kumsiaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kumsiaapp.data.remote.response.EventResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.data.remote.retrofit.ApiConfig
import com.dicoding.kumsiaapp.utils.EventLiveData
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class EventViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<EventLiveData<Boolean>>()
    val isSuccess: LiveData<EventLiveData<Boolean>> = _isSuccess

    private val _isDeleteSuccess = MutableLiveData<EventLiveData<Boolean>>()
    val isDeleteSuccess: LiveData<EventLiveData<Boolean>> = _isDeleteSuccess

    private val _eventData = MutableLiveData<EventResponseDTO>()
    val eventData: LiveData<EventResponseDTO> = _eventData

    private val _eventItemData = MutableLiveData<EventLiveData<EventsItem?>>()
    val eventItemData: LiveData<EventLiveData<EventsItem?>> = _eventItemData

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

    fun addNewEvent(token: String, photo: MultipartBody.Part, eventData: RequestBody) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().addNewEvent(token, photo, eventData)
        client.enqueue(object : retrofit2.Callback<EventsItem> {
            override fun onResponse(
                call: Call<EventsItem>,
                response: Response<EventsItem>
            ) {
                _isLoading.value = false
                _isSuccess.value = EventLiveData(response.isSuccessful)
            }
            override fun onFailure(call: Call<EventsItem>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = EventLiveData(false)
            }
        })
    }

    fun updateEvent(token: String, eventId: String, photo: MultipartBody.Part?, eventData: RequestBody) {
        _isLoading.value = true

        val client: Call<EventsItem> = if (photo == null) {
            ApiConfig.getApiService().updateEvent(eventId, token, eventData, null)
        } else {
            ApiConfig.getApiService().updateEvent(eventId, token, eventData, photo)
        }

        client.enqueue(object : retrofit2.Callback<EventsItem> {
            override fun onResponse(
                call: Call<EventsItem>,
                response: Response<EventsItem>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _eventItemData.value = EventLiveData(response.body())
                } else {
                    _eventItemData.value = EventLiveData(null)
                }
            }
            override fun onFailure(call: Call<EventsItem>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun deleteEvent(token: String, eventId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().deleteEvent(eventId, token)
        client.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                _isLoading.value = false
                _isDeleteSuccess.value = EventLiveData(response.isSuccessful)
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                _isLoading.value = false
                _isDeleteSuccess.value = EventLiveData(false)
            }
        })
    }

    fun submitEvent(token: String, eventId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().submitEvent(eventId, token)
        client.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                _isLoading.value = false
                _isSuccess.value = EventLiveData(response.isSuccessful)
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = EventLiveData(false)
            }
        })
    }
}