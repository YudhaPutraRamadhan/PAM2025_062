package com.example.hobbyyk_new.view.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyyk_new.data.api.RetrofitClient
import com.example.hobbyyk_new.data.model.Community
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var communities by mutableStateOf<List<Community>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        fetchCommunities()
    }

    fun fetchCommunities() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.getCommunities()
                if (response.isSuccessful) {
                    communities = response.body() ?: emptyList()
                    Log.d("HomeViewModel", "Dapat ${communities.size} komunitas")
                } else {
                    errorMessage = "Gagal mengambil data: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error jaringan: ${e.message}"
                Log.e("HomeViewModel", "Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}