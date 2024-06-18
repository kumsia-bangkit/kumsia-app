package com.dicoding.kumsiaapp.data.remote.request

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CommentRequestDTO(

	@field:SerializedName("comment_text")
	val commentText: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null
) : Parcelable
