package com.dicoding.kumsiaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kumsiaapp.data.remote.response.OrganizationDTO
import com.dicoding.kumsiaapp.data.remote.response.TokenResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.UserOrganization
import com.dicoding.kumsiaapp.data.remote.retrofit.ApiConfig
import com.dicoding.kumsiaapp.utils.EventLiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class ProfileViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _tokenData = MutableLiveData<EventLiveData<TokenResponseDTO?>>()
    val tokenData: LiveData<EventLiveData<TokenResponseDTO?>> = _tokenData

    private val _organizationData = MutableLiveData<EventLiveData<UserOrganization>>()
    val organizationData : LiveData<EventLiveData<UserOrganization>> = _organizationData

    fun updateOrganizationProfile(token: String, orgData: RequestBody, photo: MultipartBody.Part?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().updateOrganizationData(token, orgData, photo)
        client.enqueue(object : retrofit2.Callback<TokenResponseDTO> {
            override fun onResponse(
                call: Call<TokenResponseDTO>,
                response: Response<TokenResponseDTO>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _tokenData.value = EventLiveData(response.body())
                } else {
                    _tokenData.value = EventLiveData(null)
                }
            }
            override fun onFailure(call: Call<TokenResponseDTO>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun updateUserProfile(token: String, orgData: RequestBody, photo: MultipartBody.Part?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().updateUserData(token, orgData, photo)
        client.enqueue(object : retrofit2.Callback<TokenResponseDTO> {
            override fun onResponse(
                call: Call<TokenResponseDTO>,
                response: Response<TokenResponseDTO>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _tokenData.value = EventLiveData(response.body())
                } else {
                    _tokenData.value = EventLiveData(null)
                }
            }
            override fun onFailure(call: Call<TokenResponseDTO>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getOrganizationProfile(organizationId: String, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getOrganizationProfile(organizationId, token)
        client.enqueue(object : retrofit2.Callback<UserOrganization> {
            override fun onResponse(
                call: Call<UserOrganization>,
                response: Response<UserOrganization>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _organizationData.value = EventLiveData(response.body()!!)
                }
            }
            override fun onFailure(call: Call<UserOrganization>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}