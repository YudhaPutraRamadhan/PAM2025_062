package com.example.hobbyyk_new.data.model

data class UpdateCommunityRequest(
    val nama_komunitas: String,
    val lokasi: String,
    val deskripsi: String,
    val kategori: String,   // <--- Baru
    val kontak: String,     // <--- Baru
    val link_grup: String
)