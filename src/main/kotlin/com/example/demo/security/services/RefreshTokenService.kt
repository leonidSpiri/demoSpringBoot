package com.example.demo.security.services

import com.example.demo.exception.TokenRefreshException
import com.example.demo.models.RefreshToken
import com.example.demo.repository.RefreshTokenRepository
import com.example.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class RefreshTokenService(
    val refreshTokenRepository: RefreshTokenRepository,
    val userRepository: UserRepository
) {
    @Value("\${example.app.jwtRefreshExpirationMs}")
    private val refreshTokenDurationMs = 0L

    fun findByToken(token: String) = refreshTokenRepository.findByToken(token)

    fun createRefreshToken(userId: Long): RefreshToken {
        val refreshToken = RefreshToken(
            user = userRepository.findById(userId).get(),
            expiryDate = Instant.now().plusMillis(refreshTokenDurationMs),
            token = UUID.randomUUID().toString()
        )
        return refreshTokenRepository.save(refreshToken)
    }

    fun verifyExpiration(refreshToken: RefreshToken) =
        if (refreshToken.expiryDate < Instant.now()) {
            refreshTokenRepository.delete(refreshToken)
            throw TokenRefreshException(
                refreshToken.token,
                "Refresh token was expired. Please make a new signin request"
            )
        } else refreshToken

    @Transactional
    fun deleteByUserId(userId: Long) = refreshTokenRepository.deleteByUser(userRepository.findById(userId).get())
}