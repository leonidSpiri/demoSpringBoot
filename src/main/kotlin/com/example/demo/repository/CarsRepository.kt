package com.example.demo.repository

import com.example.demo.models.Cars
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CarsRepository : CrudRepository<Cars, Int>{

    @Query("SELECT * FROM cars WHERE name = :name", nativeQuery = true)
    fun findByName(name: String): Iterable<Cars>
}