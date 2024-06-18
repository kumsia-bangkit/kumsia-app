package com.dicoding.kumsiaapp.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class EventResponseDTO(

	@field:SerializedName("events")
	val events: List<EventsItem?>? = null
) : Parcelable

@Parcelize
data class EventsItem(

	@field:SerializedName("preference_id")
	val preferenceId: String? = null,

	@field:SerializedName("attendee_criteria")
	val attendeeCriteria: String? = null,

	@field:SerializedName("like_count")
	val likeCount: Int? = null,

	@field:SerializedName("last_edited")
	val lastEdited: String? = null,

	@field:SerializedName("religion_preference")
	val religionPreference: List<String?>? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("city_preference")
	val cityPreference: List<String?>? = null,

	@field:SerializedName("gender_preference")
	val genderPreference: List<String?>? = null,

	@field:SerializedName("capacity")
	val capacity: Int? = null,

	@field:SerializedName("hobby_preference")
	val hobbyPreference: List<String?>? = null,

	@field:SerializedName("profie_picture")
	val profilePicture: String? = null,

	@field:SerializedName("event_start")
	val eventStart: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("contact_varchar")
	val contactVarchar: String? = null,

	@field:SerializedName("organization_id")
	val organizationId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class EventUserResponseDTO(

	@field:SerializedName("events")
	val events: List<EventsItemUser?>? = null
) : Parcelable

@Parcelize
data class EventsItemUser(

	@field:SerializedName("preference_id")
	val preferenceId: String? = null,

	@field:SerializedName("attendee_criteria")
	val attendeeCriteria: String? = null,

	@field:SerializedName("like_count")
	val likeCount: Int? = null,

	@field:SerializedName("last_edited")
	val lastEdited: String? = null,

	@field:SerializedName("religion_preference")
	val religionPreference: List<String?>? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("joined")
	val joined: Boolean? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("organization_name")
	val organizationName: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("city_preference")
	val cityPreference: List<String?>? = null,

	@field:SerializedName("liked")
	val liked: Boolean? = null,

	@field:SerializedName("gender_preference")
	val genderPreference: List<String?>? = null,

	@field:SerializedName("capacity")
	val capacity: Int? = null,

	@field:SerializedName("hobby_preference")
	val hobbyPreference: List<String?>? = null,

	@field:SerializedName("profie_picture")
	val profiePicture: String? = null,

	@field:SerializedName("event_start")
	val eventStart: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("contact_varchar")
	val contactVarchar: String? = null,

	@field:SerializedName("organization_id")
	val organizationId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
