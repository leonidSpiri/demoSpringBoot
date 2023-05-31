package com.example.demo

import kotlin.random.Random

data class Cars(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String
) {
    constructor(name: String, price: Int, description: String) : this(
        Random.nextInt(),
        name,
        price,
        description
    )
}
