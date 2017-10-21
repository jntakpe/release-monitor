package com.github.jntakpe.releasemonitor.config

import com.github.jntakpe.releasemonitor.web.API
import com.github.jntakpe.releasemonitor.web.APPLICATIONS
import com.github.jntakpe.releasemonitor.web.ApplicationHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class RoutesConfiguration(private val applicationHandler: ApplicationHandler) {

    @Bean
    fun apiRouter() = router {
        API.and(accept(MediaType.APPLICATION_JSON)).nest {
            APPLICATIONS.nest {
                GET("/", applicationHandler::findAll)
                POST("/", applicationHandler::create)
                PUT("/{id}", applicationHandler::update)
                DELETE("/{id}", applicationHandler::delete)
            }
        }
    }
}