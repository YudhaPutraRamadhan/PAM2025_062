package com.example.hobbyyk_new.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyyk_new.data.api.RetrofitClient
import com.example.hobbyyk_new.data.model.Community
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AdminCommunityViewModel : ViewModel() {
    var myCommunity by mutableStateOf<Community?>(null)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchMyCommunity() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.getMyCommunity()
                if (response.isSuccessful) {
                    myCommunity = response.body()
                } else {
                    errorMessage = "Gagal memuat data"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun createCommunity(
        nama: String, lokasi: String, deskripsi: String,
        kategori: String, kontak: String, linkGrup: String,
        logoFile: File, bannerFile: File
    ) {
        viewModelScope.launch {
            isLoading = true
            try {
                val namaPart = nama.toRequestBody("text/plain".toMediaTypeOrNull())
                val lokasiPart = lokasi.toRequestBody("text/plain".toMediaTypeOrNull())
                val deskripsiPart = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
                val kategoriPart = kategori.toRequestBody("text/plain".toMediaTypeOrNull())
                val kontakPart = kontak.toRequestBody("text/plain".toMediaTypeOrNull())
                val linkGrupPart = linkGrup.toRequestBody("text/plain".toMediaTypeOrNull())

                val requestLogo = logoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val logoMultipart = MultipartBody.Part.createFormData("file", logoFile.name, requestLogo)

                val requestBanner = bannerFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val bannerName = "banner_${System.currentTimeMillis()}.jpg"
                val bannerMultipart = MultipartBody.Part.createFormData("banner", bannerName, requestBanner)

                val response = RetrofitClient.instance.createCommunity(
                    namaPart, lokasiPart, deskripsiPart,
                    kategoriPart, kontakPart, linkGrupPart,
                    logoMultipart, bannerMultipart
                )

                if (response.isSuccessful) {
                    fetchMyCommunity()
                } else {
                    errorMessage = "Gagal membuat: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteCommunity(id: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.deleteCommunity(id)
                if (response.isSuccessful) {
                    myCommunity = null
                } else {
                    errorMessage = "Gagal menghapus"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateCommunity(
        id: Int, nama: String, lokasi: String, deskripsi: String,
        kategori: String, kontak: String, linkGrup: String,
        newLogoFile: File?, newBannerFile: File?
    ) {
        viewModelScope.launch {
            isLoading = true
            try {
                val namaPart = nama.toRequestBody("text/plain".toMediaTypeOrNull())
                val lokasiPart = lokasi.toRequestBody("text/plain".toMediaTypeOrNull())
                val deskripsiPart = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
                val kategoriPart = kategori.toRequestBody("text/plain".toMediaTypeOrNull())
                val kontakPart = kontak.toRequestBody("text/plain".toMediaTypeOrNull())
                val linkGrupPart = linkGrup.toRequestBody("text/plain".toMediaTypeOrNull())

                var logoMultipart: MultipartBody.Part? = null
                if (newLogoFile != null) {
                    val requestLogo = newLogoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val fileName = "logo_${System.currentTimeMillis()}.jpg"
                    logoMultipart = MultipartBody.Part.createFormData("file", fileName, requestLogo)
                }

                var bannerMultipart: MultipartBody.Part? = null
                if (newBannerFile != null) {
                    val requestBanner = newBannerFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val fileName = "banner_${System.currentTimeMillis()}.jpg"
                    bannerMultipart = MultipartBody.Part.createFormData("banner", fileName, requestBanner)
                }

                val response = RetrofitClient.instance.updateCommunity(
                    id, namaPart, lokasiPart, deskripsiPart, kategoriPart, kontakPart, linkGrupPart,
                    logoMultipart, bannerMultipart
                )

                if (response.isSuccessful) {
                    fetchMyCommunity()
                } else {
                    errorMessage = "Gagal update: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}