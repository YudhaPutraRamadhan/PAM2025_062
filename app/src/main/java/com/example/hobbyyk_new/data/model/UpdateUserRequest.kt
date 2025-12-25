package com.example.hobbyyk_new.data.model

data class UpdateUserRequest(
    val username: String,
    val email: String,
    val role: String,
    @com.google.gson.annotations.SerializedName("is_verified")
    val isVerified: Boolean
)