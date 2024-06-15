package com.dicoding.kumsiaapp.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class EventResponseDTO(

	@field:SerializedName("attendee_criteria")
	val attendeeCriteria: String? = null,

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

	@field:SerializedName("event_start")
	val eventStart: String? = null,

	@field:SerializedName("contact")
	val contact: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: String? = null
) : Parcelable
