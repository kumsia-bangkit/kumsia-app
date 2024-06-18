package com.dicoding.kumsiaapp.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class FriendsListResponseDTO(

	@field:SerializedName("friends")
	val friends: List<FriendsItem?>? = null
) : Parcelable

@Parcelize
data class FriendsItem(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable
