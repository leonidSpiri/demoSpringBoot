package com.example.demo

import com.example.demo.models.Cars
import com.example.demo.repository.CarsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarsService(val carsRepository: CarsRepository){

    fun getAllCars(): Iterable<Cars> = carsRepository.findAll().sortedBy { it.id }

    fun getCarById(id: Int) = carsRepository.findById(id)

    fun getCarByName(name: String) = carsRepository.findByName(name)

    fun addCar(car: Cars) = carsRepository.save(car)

    fun updateCar(id: Int, car: Cars) =
        if (!carsRepository.existsById(id))
            carsRepository.save(car)
        else carsRepository.save(car)

    fun deleteCar(id: Int) = carsRepository.deleteById(id)

    fun <T : Any> Optional<out T>.toList(): List<T> =
        if (isPresent) listOf(get()) else emptyList()
}