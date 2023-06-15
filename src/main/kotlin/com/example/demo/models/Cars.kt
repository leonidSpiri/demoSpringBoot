package com.example.demo.models

import jakarta.persistence.*

@Entity
@Table(name = "cars")
class Cars(
    @field:Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    val name: String,
    val price: Int,
    val description: String
)