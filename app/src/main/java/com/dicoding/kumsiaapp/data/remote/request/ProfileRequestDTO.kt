package com.dicoding.kumsiaapp.data.remote.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateOrgProfileDTO(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("contact")
    val contact: String? = null
) : Parcelable

@Parcelize
data class UpdateUserProfileDTO(

    @field:SerializedName("religion_preference")
    var religionPreference: List<String?>? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("city_preference")
    var cityPreference: List<String?>? = null,

    @field:SerializedName("gender_preference")
    var genderPreference: List<String?>? = null,

    @field:SerializedName("religion")
    val religion: String? = null,

    @field:SerializedName("hobby_preference")
    var hobbyPreference: List<String?>? = null,

    @field:SerializedName("dob")
    val dob: String? = null,

    @field:SerializedName("contact")
    val contact: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("guardian_contact")
    val guardianContact: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("new_password")
    val newPassword: String? = null,

    @field:SerializedName("password")
    val password: String? = null
) : Parcelable