package com.example.hobbyyk_new.data.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confPassword: String,
    val role: String
)