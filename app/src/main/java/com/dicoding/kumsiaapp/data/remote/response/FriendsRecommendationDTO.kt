package com.dicoding.kumsiaapp.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class FriendsRecommendationDTO(

	@field:SerializedName("users")
	val users: List<UsersItem?>? = null
) : Parcelable

@Parcelize
data class UsersItem(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("hobbies")
	val hobbies: List<String?>? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("religion")
	val religion: String? = null
) : Parcelable
