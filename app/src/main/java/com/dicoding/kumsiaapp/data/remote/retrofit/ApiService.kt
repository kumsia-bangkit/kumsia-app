package com.dicoding.kumsiaapp.data.remote.retrofit

import com.dicoding.kumsiaapp.data.remote.request.IndividualRegisterDTO
import com.dicoding.kumsiaapp.data.remote.request.OrganizationRegisterDTO
import com.dicoding.kumsiaapp.data.remote.response.LoginResponseDTO
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    // Authentication
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String): Call<LoginResponseDTO>
    @POST("register")
    fun registerForIndividual(@Body registerDTO: IndividualRegisterDTO): Call<JsonObject>
    @POST("organization/register")
    fun registerForOrganization(@Body registerDTO: OrganizationRegisterDTO): Call<JsonObject>
}