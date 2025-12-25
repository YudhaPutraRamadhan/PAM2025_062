package com.example.hobbyyk_new.data.model

data class CreateUserRequest(
    val username: String,
    val email: String,
    val role: String
)