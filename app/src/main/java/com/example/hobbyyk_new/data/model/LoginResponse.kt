package com.example.hobbyyk_new.data.model

data class LoginResponse(
    val accessToken: String,
    val role: String,
    val userId: Int,
    val username: String
)