package com.example.demo.security.jwt

import com.example.demo.security.services.UserDetailsImpl
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {

    private val logger = LoggerFactory.getLogger(JwtUtils::class.java)

    @Value("\${example.app.jwtSecret}")
    private val jwtSecret = ""

    @Value("\${example.app.jwtExpirationMs}")
    private val jwtExpirationMs = 0

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl
        return generateTokenFromUsername(userPrincipal.username)
    }

    fun generateTokenFromUsername(username: String) = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(Date())
        .setExpiration(Date(Date().time + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS512)
        .compact()

    private fun key() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))

    fun getUserNameFromJwtToken(token: String?): String =
        Jwts.parserBuilder().setSigningKey(key()).build()
            .parseClaimsJws(token).body.subject


    fun validateJwtToken(authToken: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken)
            return true

        } catch (e: SecurityException) {
            logger.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }
}
