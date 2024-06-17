package com.dicoding.kumsiaapp.data.remote.retrofit

import com.dicoding.kumsiaapp.data.remote.request.IndividualRegisterDTO
import com.dicoding.kumsiaapp.data.remote.request.OrganizationRegisterDTO
import com.dicoding.kumsiaapp.data.remote.response.CommentResponseDTO
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
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @Multipart
    @PUT("events/org/update/{event_id}")
    fun updateEvent(
        @Path("event_id") eventId: String,
        @Header("access-token") token: String,
        @Part("event") eventData: RequestBody,
        @Part photoFile: MultipartBody.Part?
    ) : Call<EventsItem>

    @DELETE("events/org/delete")
    fun deleteEvent(
        @Query("event_id") eventId: String,
        @Header("access-token") token: String
    ) : Call<JsonObject>

    @PUT("events/org/submit")
    fun submitEvent(
        @Query("event_id") eventId: String,
        @Header("access-token") token: String
    ) : Call<JsonObject>

    @PUT("events/org/cancel")
    fun cancelEvent(
        @Query("event_id") eventId: String,
        @Header("access-token") token: String
    ) : Call<JsonObject>

    // Comments
    @GET("comment/all")
    fun getAllComments(
        @Query("event_id") eventId: String
    ): Call<CommentResponseDTO>

}