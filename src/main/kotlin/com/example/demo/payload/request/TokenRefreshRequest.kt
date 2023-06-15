package com.example.demo.payload.request

import jakarta.validation.constraints.NotBlank

data class TokenRefreshRequest(
    @NotBlank
    val refreshToken: String
)
