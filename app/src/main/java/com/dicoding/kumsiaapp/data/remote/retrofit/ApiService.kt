package com.dicoding.kumsiaapp.data.remote.retrofit

import com.dicoding.kumsiaapp.data.remote.request.IndividualRegisterDTO
import com.dicoding.kumsiaapp.data.remote.request.OrganizationRegisterDTO
import com.dicoding.kumsiaapp.data.remote.response.EventResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.data.remote.response.LoginResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.OrganizationDTO
import com.dicoding.kumsiaapp.data.remote.response.UserDTO
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    // Authentication
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String): Call<LoginResponseDTO>
    @POST("register")
    fun registerForIndividual(@Body registerDTO: IndividualRegisterDTO): Call<JsonObject>
    @POST("register/organization")
    fun registerForOrganization(@Body registerDTO: OrganizationRegisterDTO): Call<JsonObject>
    @GET("users")
    fun getLoggedInUser(
        @Header(value = "token") token: String
    ): Call<UserDTO>
    @GET("users")
    fun getLoggedInOrganization(
        @Header(value = "token") token: String
    ): Call<OrganizationDTO>

    // Events (Organization)
    @GET("events/org/all")
    fun getAllOrganizationEvents(
        @Header(value = "access-token") token: String
    ): Call<EventResponseDTO>

    @Multipart
    @POST("events/org/new")
    fun addNewEvent(
        @Header("access-token") token: String,
        @Part photoFile: MultipartBody.Part,
        @Part("event") eventData: RequestBody
    ) : Call<EventsItem>

}