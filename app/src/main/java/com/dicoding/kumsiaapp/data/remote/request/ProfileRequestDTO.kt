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