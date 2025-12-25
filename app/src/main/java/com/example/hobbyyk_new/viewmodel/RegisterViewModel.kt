package com.example.hobbyyk_new.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyyk_new.data.api.RetrofitClient
import com.example.hobbyyk_new.data.model.RegisterRequest
import com.example.hobbyyk_new.data.model.VerifyOtpRequest
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confPassword by mutableStateOf("")

    var navigateToOtp by mutableStateOf<String?>(null)
    var otpCode by mutableStateOf("")
    var showOtpDialog by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun register() {
        if (password != confPassword) {
            errorMessage = "Password dan Confirm Password tidak sama!"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val request = RegisterRequest(
                    username = name,
                    email = email,
                    password = password,
                    confPassword = confPassword,
                    role = "user"
                )
                val response = RetrofitClient.instance.register(request)

                if (response.isSuccessful) {
                    navigateToOtp = email
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage = "Gagal Daftar: $errorBody"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}