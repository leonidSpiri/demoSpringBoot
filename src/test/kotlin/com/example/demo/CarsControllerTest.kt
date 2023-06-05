package com.example.demo

import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CarsControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var carsRepository: CarsRepository
    val carsArray: Iterable<Cars> = listOf(
        Cars(
            1,
            "BMW",
            1000000,
            "BMW is a German multinational company which produces luxury vehicles and motorcycles."
        ),
        Cars(
            2,
            "Mercedes",
            2000000,
            "Mercedes-Benz is a German multinational company which produces luxury vehicles and motorcycles."
        ),
        Cars(
            3,
            "Audi",
            3000000,
            "Audi is a German multinational company which produces luxury vehicles and motorcycles."
        ),
        Cars(
            4,
            "Lamborghini",
            4000000,
            "Lamborghini is an Italian multinational company which produces luxury vehicles and motorcycles."
        ),
        Cars(
            5,
            "Ferrari",
            5000000,
            "Ferrari is an Italian multinational company which produces luxury vehicles and motorcycles."
        )
    )

    @Test
    fun getAllCars() {
        carsRepository.findAll().forEach { cars ->
            carsArray.find { it.id == cars.id }?.let {
                assertEquals(it.name, cars.name)
                assertEquals(it.price, cars.price)
                assertEquals(it.description, cars.description)
            }
        }
        webTestClient.get().uri("/cars").exchange().expectStatus().isOk.expectBody(Iterable::class.java)
            .returnResult().responseBody?.iterator()?.forEach{ cars ->
              println(cars)
            }
    }

    @Test
    fun getCarById() {
        val id = 2
        assertEquals("Mercedes", carsRepository.findById(id).get().name)
    }

    @Test
    fun addCar() {

        webTestClient.post().uri("/cars")
            .bodyValue(
                Cars(
                    6,
                    "Porsche",
                    6000000,
                    "Porsche is a German multinational company which produces luxury vehicles and motorcycles."
                )
            )
            .exchange().expectStatus().isCreated
        assert(carsRepository.existsById(6))
        assert(carsRepository.findById(6).get().name == "Porsche")
    }

    @Test
    fun updateCar() {
        carsRepository.save(
            Cars(
                1,
                "BMW-1",
                1,
                "BMW is a German multinational company which produces luxury vehicles and motorcycles."
            )
        )

        assertEquals("BMW-1", carsRepository.findById(1).get().name)
    }

    @Test
    fun deleteCar() {
        carsRepository.deleteById(1)
        assertEquals(carsRepository.findById(1).isEmpty, true)
    }
}