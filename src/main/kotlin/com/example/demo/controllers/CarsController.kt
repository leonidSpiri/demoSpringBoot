package com.example.demo.controllers

import com.example.demo.CarsService
import com.example.demo.models.Cars
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger
import kotlin.jvm.optionals.toList

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/cars")
class CarsController(private val carsService: CarsService) {
    val logger: Logger = Logger.getLogger(CarsController::class.java.canonicalName)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCars() =
        carsService.getAllCars().also { carList ->
            carList.toList().forEach { car -> logger.info("${car.id} ${car.name} ${car.price} ${car.description}") }
        }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    fun getCarById(@PathVariable id: Int) = carsService.getCarById(id)
        .also { carList ->
            carList.toList().forEach { car -> logger.info("${car.id} ${car.name} ${car.price} ${car.description}") }
        }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    fun getInfo(@RequestHeader headers: Map<String, String>, response: HttpServletResponse) {
        val requestInfoString = headers.map { "${it.key}: ${it.value}" }.joinToString("\n")
        response.writer.write(requestInfoString)
        response.status = HttpStatus.OK.value()
        response.contentType = "text/plain"
        response.characterEncoding = "UTF-8"
        logger.info("Request info: $requestInfoString")
        return response.writer.close()
    }

    @GetMapping("/name/{name}")
    fun getCarByName(@PathVariable name: String): ResponseEntity<List<Cars>> {
        val carList = carsService.getCarByName(name).toList()
        return if (carList.isEmpty()) {
            logger.info("Car not found")
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            carList.forEach { car -> logger.info("${car.id} ${car.name} ${car.price} ${car.description}") }
            ResponseEntity(carList, HttpStatus.OK)
        }
    }


    @PreAuthorize("#request.getRemoteAddr().equals('127.0.0.1') and hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCar(@RequestBody car: Cars, request: HttpServletRequest) =
        carsService.addCar(car)
            .also { carRes ->
                logger.info("${carRes.id} ${carRes.name} ${carRes.price} ${carRes.description}")
                logger.info(request.remoteAddr)
            }


    @PutMapping("/{id}")
    fun updateCar(@PathVariable id: Int, @RequestBody car: Cars): ResponseEntity<Cars> {
        val status = if (!carsService.getCarById(id).isPresent)
            HttpStatus.CREATED
        else HttpStatus.OK
        val body = carsService.updateCar(id, car)
            .also { carRes -> logger.info("$status ${carRes.id} ${carRes.name} ${carRes.price} ${carRes.description}") }
        return ResponseEntity(body, status)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCar(@PathVariable id: Int) = carsService.deleteCar(id).also { logger.info("Car deleted") }
}