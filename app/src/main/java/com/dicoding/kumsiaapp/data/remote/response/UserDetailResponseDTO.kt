package com.dicoding.kumsiaapp.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserDetailResponseDTO(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("is_friends")
	val isFriends: Int? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("preference_gender")
	val preferenceGender: List<String?>? = null,

	@field:SerializedName("religion")
	val religion: String? = null,

	@field:SerializedName("preference_hobby")
	val preferenceHobby: List<String?>? = null,

	@field:SerializedName("last_activity")
	val lastActivity: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("contact")
	val contact: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("preference_city")
	val preferenceCity: List<String?>? = null,

	@field:SerializedName("preference_religion")
	val preferenceReligion: List<String?>? = null,

	@field:SerializedName("guardian_contact")
	val guardianContact: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable
