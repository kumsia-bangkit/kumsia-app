package com.dicoding.kumsiaapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponseDTO(
	@field:SerializedName("access_token")
	val accessToken: String? = null
)

data class TokenResponseDTO(
	@field:SerializedName("access_token")
	val accessToken: String? = null
)

