package com.dicoding.kumsiaapp.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class OrganizationDTO(

	@field:SerializedName("user")
	val user: UserOrganization? = null
) : Parcelable

@Parcelize
data class UserOrganization(

	@field:SerializedName("is_new_user")
	val isNewUser: String? = null,

	@field:SerializedName("organization_id")
	val organizationId: String? = null,

	@field:SerializedName("contact")
	val contact: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable
