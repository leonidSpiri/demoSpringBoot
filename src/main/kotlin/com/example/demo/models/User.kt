package com.example.demo.models

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["username", "email"])
    ])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @NotBlank
    @Column(name = "username")
    val userName: String,

    @NotBlank
    @Email
    @Column(name = "email")
    val email: String,

    @NotBlank
    val password: String,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")])
    val roles: Set<Role>
)