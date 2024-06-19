package com.dicoding.kumsiaapp.data.remote.retrofit

import com.dicoding.kumsiaapp.data.remote.request.CommentRequestDTO
import com.dicoding.kumsiaapp.data.remote.request.IndividualRegisterDTO
import com.dicoding.kumsiaapp.data.remote.request.OrganizationRegisterDTO
import com.dicoding.kumsiaapp.data.remote.response.CommentResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.EventResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.EventUserResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.data.remote.response.EventsItemUser
import com.dicoding.kumsiaapp.data.remote.response.FriendsListResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.LoginResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.OrganizationDTO
import com.dicoding.kumsiaapp.data.remote.response.TokenResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.UserDTO
import com.dicoding.kumsiaapp.data.remote.response.UserDetailResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.UserIndividual
import com.dicoding.kumsiaapp.data.remote.response.UserOrganization
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
import retrofit2.http.PATCH
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

    // Profile
    @GET("profile/user/{user_id}")
    fun getUserProfile(
        @Path(value = "user_id") userId: String,
        @Header(value = "access-token") token: String
    ): Call<UserDetailResponseDTO>

    @GET("profile/organization/{organization_id}")
    fun getOrganizationProfile(
        @Path(value = "organization_id") orgId: String,
        @Header(value = "access-token") token: String
    ): Call<UserOrganization>

    @Multipart
    @PATCH("profile/organization/update")
    fun updateOrganizationData(
        @Header("access-token") token: String,
        @Part("request") orgData: RequestBody,
        @Part photoFile: MultipartBody.Part?
    ): Call<TokenResponseDTO>

    @Multipart
    @PATCH("profile/user/update")
    fun updateUserData(
        @Header("access-token") token: String,
        @Part("request") userData: RequestBody,
        @Part photoFile: MultipartBody.Part?
    ): Call<TokenResponseDTO>

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

    // Events (User)
    @GET("events/user/all")
    fun getAllEventsForUser(
        @Header(value = "access-token") token: String
    ): Call<EventUserResponseDTO>

    @GET("events/user/joined")
    fun getAllUserJoinedEvents(
        @Header(value = "access-token") token: String
    ): Call<EventUserResponseDTO>

    @POST("events/user/join")
    fun joinEvent(
        @Query("event_id") eventId: String,
        @Header(value = "access-token") token: String
    ): Call<EventsItemUser>

    @DELETE("events/user/unjoin")
    fun unjoinEvent(
        @Query("event_id") eventId: String,
        @Header(value = "access-token") token: String
    ): Call<EventsItemUser>

    // Comments
    @GET("comment/all")
    fun getAllComments(
        @Query("event_id") eventId: String
    ): Call<CommentResponseDTO>

    @POST("comment/create")
    fun createComment(
        @Header(value = "access-token") token: String,
        @Body commentDTO : CommentRequestDTO
    ): Call<JsonObject>

    // Friends
    @GET("friends/")
    fun getAllFriends(
        @Header(value = "access-token") token: String
    ): Call<FriendsListResponseDTO>

    @GET("friends/request")
    fun getAllFriendRequests(
        @Header(value = "access-token") token: String
    ): Call<FriendsListResponseDTO>

    @POST("friends/send")
    fun sendFriendRequest(
        @Query("friend_id") friendId: String,
        @Header(value = "access-token") token: String
    ): Call<JsonObject>

    @PUT("friends/accept")
    fun acceptFriendRequest(
        @Query("friend_id") friendId: String,
        @Header(value = "access-token") token: String
    ): Call<JsonObject>

    @PUT("friends/reject")
    fun rejectFriendRequest(
        @Query("friend_id") friendId: String,
        @Header(value = "access-token") token: String
    ): Call<JsonObject>

    // Like Event
    @POST("like/create")
    fun likeEvent(
        @Query("event_id") eventId: String,
        @Header(value = "access-token") token: String
    ): Call<JsonObject>

    @DELETE("like/delete")
    fun unlikeEvent(
        @Query("event_id") eventId: String,
        @Header(value = "access-token") token: String
    ): Call<JsonObject>

}