package com.example.demo.payload.response

data class JwtResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val id: Long,
    val username: String,
    val email: String,
    val roles: List<String>
)