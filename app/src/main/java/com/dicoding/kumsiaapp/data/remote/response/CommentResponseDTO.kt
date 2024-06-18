package com.dicoding.kumsiaapp.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CommentResponseDTO(

	@field:SerializedName("comments")
	var comments: List<CommentsItem?>? = null
) : Parcelable

@Parcelize
data class CommentsItem(

	@field:SerializedName("comment_text")
	val commentText: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("user_picture")
	val userPicture: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("comment_id")
	val commentId: String? = null
) : Parcelable
