package com.example.demo

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cars")
class RestApiDemoController(private val carsRepository: CarsRepository) {

    @GetMapping
    fun getAllCars(): Iterable<Cars> = carsRepository.findAll()

    @GetMapping("/{id}")
    fun getCarById(@PathVariable id: Int) = carsRepository.findById(id)

    @PostMapping
    fun addCar(@RequestBody car: Cars) = carsRepository.save(car)

    @PutMapping("/{id}")
    fun updateCar(@PathVariable id: Int, @RequestBody car: Cars) =
        if (!carsRepository.existsById(id))
            ResponseEntity(carsRepository.save(car), HttpStatus.CREATED)
        else ResponseEntity(carsRepository.save(car), HttpStatus.OK)

    @DeleteMapping("/{id}")
    fun deleteCar(@PathVariable id: Int) = carsRepository.deleteById(id)
}