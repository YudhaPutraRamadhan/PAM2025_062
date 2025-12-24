package com.example.hobbyyk_new.data.model

data class Community(
    val id: Int,
    val nama_komunitas: String,
    val deskripsi: String,
    val lokasi: String,
    val foto_url: String?,
    val banner_url: String?,
    val link_grup: String?
)