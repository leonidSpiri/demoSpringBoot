package com.example.demo.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignUpRequest(
    @NotBlank
    @Size(min = 3, max = 20)
    val userName: String,

    @NotBlank
    @Size(min = 6, max = 40)
    val password: String,

    @NotBlank
    @Size(max = 50)
    val email: String,

    val roles: Set<String>
)