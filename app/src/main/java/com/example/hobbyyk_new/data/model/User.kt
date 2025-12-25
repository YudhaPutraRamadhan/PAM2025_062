package com.example.hobbyyk_new.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    @SerializedName("is_verified")
    val isVerified: Boolean
)