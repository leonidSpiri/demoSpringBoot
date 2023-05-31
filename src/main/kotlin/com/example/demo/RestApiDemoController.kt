package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cars")
class RestApiDemoController {
    private val carList = mutableListOf<Cars>()

    init {
        carList.add(Cars("Toyota Camry", 120000, "2012"))
        carList.add(Cars("Honda Accord", 13234, "2013"))
        carList.add(Cars("Nissan Altima", 234987, "2014"))
        carList.add(Cars("Ford Fusion", 234987, "2008"))
        carList.add(Cars("Hyundai Sonata", 2345687, "2013"))
        carList.add(Cars("Chevrolet Malibu", 256987, "2017"))
        carList.add(Cars("Hyundai Solaris", 23593, "2011"))
    }

    @GetMapping("/all")
    fun getAllCars() = carList

    @GetMapping("/{name}")
    fun getCarByName(@PathVariable name: String) = carList.filter { it.name == name }
}