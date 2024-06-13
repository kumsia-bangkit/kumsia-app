package com.dicoding.kumsiaapp.data.remote.request

data class IndividualRegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val name: String,
    val dob: String,
    val gender: String
)
data class OrganizationRegisterDTO(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)

