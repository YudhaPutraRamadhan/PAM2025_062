package com.example.hobbyyk_new.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyyk_new.data.api.RetrofitClient
import com.example.hobbyyk_new.data.model.VerifyOtpRequest
import kotlinx.coroutines.launch

class VerifyOtpViewModel : ViewModel() {
    var otpCode by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun verifyOtp(email: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val request = VerifyOtpRequest(email, otpCode)
                val response = RetrofitClient.instance.verifyOtp(request)

                if (response.isSuccessful) {
                    isSuccess = true
                } else {
                    errorMessage = "OTP Salah atau Kadaluarsa!"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}