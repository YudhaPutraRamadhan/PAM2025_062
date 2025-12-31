package com.example.hobbyyk_new.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    private val _isSessionExpired = MutableStateFlow(false)
    val isSessionExpired = _isSessionExpired.asStateFlow()

    fun logout() {
        _isSessionExpired.value = true
    }

    fun reset() {
        _isSessionExpired.value = false
    }
}