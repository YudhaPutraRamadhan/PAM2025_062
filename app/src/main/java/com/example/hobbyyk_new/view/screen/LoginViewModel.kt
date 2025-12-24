package com.example.hobbyyk_new.view.screen

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyyk_new.data.api.RetrofitClient
import com.example.hobbyyk_new.data.datastore.UserStore
import com.example.hobbyyk_new.data.model.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userStore = UserStore(application)

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoginSuccess by mutableStateOf(false)

    var userRole by mutableStateOf("")

    fun login() {
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage = "Email dan Password harus diisi!"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.instance.login(request)

                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        userStore.saveSession(data.accessToken, data.role, data.userId)

                        RetrofitClient.authToken = data.accessToken

                        userRole = data.role

                        Log.d("LoginViewModel", "Login Sukses! Role: ${data.role}, ID: ${data.userId}")

                        isLoginSuccess = true
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage = "Login Gagal: $errorBody"
                    Log.e("LoginViewModel", "Error: $errorBody")
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
                Log.e("LoginViewModel", "Exception: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}