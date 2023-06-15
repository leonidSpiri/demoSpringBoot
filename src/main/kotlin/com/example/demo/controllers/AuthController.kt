package com.example.demo.controllers

import com.example.demo.models.Role
import com.example.demo.models.Roles
import com.example.demo.models.User
import com.example.demo.payload.request.LoginRequest
import com.example.demo.payload.request.SignUpRequest
import com.example.demo.payload.response.JwtResponse
import com.example.demo.payload.response.MessageResponse
import com.example.demo.repository.RoleRepository
import com.example.demo.repository.UserRepository
import com.example.demo.security.jwt.JwtUtils
import com.example.demo.security.services.UserDetailsImpl
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController(
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val encoder: PasswordEncoder,
    val jwtUtils: JwtUtils
) {
    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.userName, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwtToken = jwtUtils.generateJwtToken(authentication)

        val userDetails = authentication.principal as UserDetailsImpl
        val roles = userDetails.authorities.stream()
            .map { item -> item.authority }
            .collect(Collectors.toList())

        val responseEntity = ResponseEntity.ok(
            JwtResponse(
                accessToken = jwtToken,
                id = userDetails.getId(),
                username = userDetails.username,
                email = userDetails.getEmail(),
                roles = roles
            )
        )
        return ResponseEntity.ok(responseEntity)
    }


    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> {
        if (userRepository.existsByUserName(signUpRequest.userName))
            return ResponseEntity.badRequest().body("Error: Username is already taken!")

        if (userRepository.existsByEmail(signUpRequest.email))
            return ResponseEntity.badRequest().body("Error: Email is already in use!")


        val user = User(
            userName = signUpRequest.userName,
            email = signUpRequest.email,
            password = encoder.encode(signUpRequest.password)
        )

        val strRoles = signUpRequest.roles
        val roles = mutableListOf<Role>()

        if (strRoles.isNotEmpty())
            strRoles.forEach { role ->
                when (role) {
                    "admin" -> {
                        val adminRole =
                            roleRepository.findByRole(Roles.ROLE_ADMIN)
                        roles.add(adminRole)
                    }

                    else -> {
                        val userRole = roleRepository.findByRole(Roles.ROLE_USER)
                        roles.add(userRole)
                    }
                }
            }
        else
            roles.add(roleRepository.findByRole(Roles.ROLE_USER))

        user.roles = roles.toSet()
        println(user.toString())
        userRepository.save(user)

        return ResponseEntity.ok(MessageResponse(message = "User registered successfully!"))
    }
}