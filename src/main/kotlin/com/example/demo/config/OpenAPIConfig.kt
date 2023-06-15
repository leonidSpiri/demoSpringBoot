package com.example.demo.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {
    @Value("\${example.openapi.dev-url}")
    private val devUrl: String? = null

    @Bean
    fun myOpenAPI(): OpenAPI? {
        val devServer = Server()
        devServer.url = devUrl
        devServer.description = "Server URL in Development environment"

        val contact = Contact()
        contact.email = "2003.leonid2003@gmail.com"
        contact.name = "Leonid"
        contact.url = "https://github.com/leonidSpiri"
        val mitLicense = License().name("MIT License").url("https://choosealicense.com/licenses/mit/")
        val info = Info()
            .title("Demo API")
            .version("1.0")
            .contact(contact)
            .description("This API exposes endpoints to manage tutorials.")
            .license(mitLicense)
        return OpenAPI().info(info).servers(listOf(devServer))
    }
}