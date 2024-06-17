package com.dicoding.kumsiaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.kumsiaapp.data.remote.request.IndividualRegisterDTO
import com.dicoding.kumsiaapp.data.remote.request.OrganizationRegisterDTO
import com.dicoding.kumsiaapp.data.remote.response.LoginResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.OrganizationDTO
import com.dicoding.kumsiaapp.data.remote.response.UserDTO
import com.dicoding.kumsiaapp.data.remote.retrofit.ApiConfig
import com.dicoding.kumsiaapp.utils.EventLiveData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

class AuthViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerMessage = MutableLiveData<EventLiveData<String>>()
    val registerMessage : LiveData<EventLiveData<String>> = _registerMessage

    private val _loginResponse = MutableLiveData<EventLiveData<Any>>()
    val loginResponse : LiveData<EventLiveData<Any>> = _loginResponse

    private val _userData = MutableLiveData<EventLiveData<UserDTO>>()
    val userData : LiveData<EventLiveData<UserDTO>> = _userData

    private val _organizationData = MutableLiveData<EventLiveData<OrganizationDTO>>()
    val organizationData : LiveData<EventLiveData<OrganizationDTO>> = _organizationData

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
                    _registerMessage.value = EventLiveData("Account is successfully registered")
                } else {
                    _registerMessage.value = EventLiveData("Email or username has been used")
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                _isLoading.value = false
                _registerMessage.value = EventLiveData("Something is wrong.")
            }
        })
    }

    fun registerForIndividual(registerDTO: IndividualRegisterDTO) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().registerForIndividual(registerDTO)
        client.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerMessage.value = EventLiveData("Account is successfully registered")
                } else {
                    _registerMessage.value = EventLiveData("Email or username has been used")
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                _isLoading.value = false
                _registerMessage.value = EventLiveData("Something is wrong.")
            }
        })
    }

    fun login(username: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(username, password)
        client.enqueue(object : retrofit2.Callback<LoginResponseDTO> {
            override fun onResponse(
                call: Call<LoginResponseDTO>,
                response: Response<LoginResponseDTO>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = EventLiveData(response.body()!!)
                } else {
                    when (response.code()) {
                        401 -> _loginResponse.value = EventLiveData("Password is incorrect")
                        404 -> _loginResponse.value = EventLiveData("Username doesn't exist")
                        else -> _loginResponse.value = EventLiveData("Something is wrong.")
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponseDTO>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getUserData(token: String) {
        val client = ApiConfig.getApiService().getLoggedInUser(token)
        client.enqueue(object : retrofit2.Callback<UserDTO> {
            override fun onResponse(
                call: Call<UserDTO>,
                response: Response<UserDTO>
            ) {
                if (response.isSuccessful) {
                    _userData.value = EventLiveData(response.body()!!)
                }
            }
            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getOrganizationData(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getLoggedInOrganization(token)
        client.enqueue(object : retrofit2.Callback<OrganizationDTO> {
            override fun onResponse(
                call: Call<OrganizationDTO>,
                response: Response<OrganizationDTO>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _organizationData.value = EventLiveData(response.body()!!)
                }
            }
            override fun onFailure(call: Call<OrganizationDTO>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}