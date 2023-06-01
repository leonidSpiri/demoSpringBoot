package com.example.demo

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "cars")
class Cars(
    @Id
    val id: Int,
    val name: String,
    val price: Int,
    val description: String
)