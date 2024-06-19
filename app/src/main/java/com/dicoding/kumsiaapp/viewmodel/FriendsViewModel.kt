package com.dicoding.kumsiaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kumsiaapp.data.remote.response.FriendsListResponseDTO
import com.dicoding.kumsiaapp.data.remote.retrofit.ApiConfig
import com.dicoding.kumsiaapp.utils.EventLiveData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

class FriendsViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<EventLiveData<Boolean>>()
    val isSuccess: LiveData<EventLiveData<Boolean>> = _isSuccess

    private val _friendsData = MutableLiveData<FriendsListResponseDTO>()
    val friendsData: LiveData<FriendsListResponseDTO> = _friendsData

    fun getAllFriends(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllFriends(token)
        client.enqueue(object : retrofit2.Callback<FriendsListResponseDTO> {
            override fun onResponse(
                call: Call<FriendsListResponseDTO>,
                response: Response<FriendsListResponseDTO>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _friendsData.value = response.body()
                }
            }
            override fun onFailure(call: Call<FriendsListResponseDTO>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun sendFriendRequest(friendId: String, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().sendFriendRequest(friendId, token)
        client.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isSuccess.value = EventLiveData(response.isSuccessful)
                } else {
                    _isSuccess.value = EventLiveData(false)
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = EventLiveData(false)
            }
        })
    }
}