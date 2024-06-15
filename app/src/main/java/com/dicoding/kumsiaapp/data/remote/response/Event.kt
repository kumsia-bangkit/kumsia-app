package com.dicoding.kumsiaapp.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val name: String,
    val date: String,
    val type: String
): Parcelable