package com.example.hobbyyk_new.view.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyyk_new.data.api.RetrofitClient
import com.example.hobbyyk_new.data.model.Community
import kotlinx.coroutines.launch

class CommunityDetailViewModel : ViewModel() {

    var community by mutableStateOf<Community?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun getDetail(id: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.getCommunityDetail(id)
                if (response.isSuccessful) {
                    community = response.body()
                } else {
                    errorMessage = "Gagal memuat data: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}