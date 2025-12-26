package com.example.hobbyyk_new.data.model

data class ChangePasswordRequest(
    val otp: String,
    val newPassword: String,
    val confPassword: String
)

data class EmailRequest(
    val newEmail: String
)

data class VerifyEmailRequest(
    val otp: String,
    val newEmail: String
)