package com.example.demo.repository

import com.example.demo.models.Role
import com.example.demo.models.Roles
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {

    fun findByRole(role: Roles): Role
}