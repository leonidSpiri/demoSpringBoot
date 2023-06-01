package com.example.demo

import org.springframework.data.repository.CrudRepository

interface CarsRepository : CrudRepository<Cars, Int>