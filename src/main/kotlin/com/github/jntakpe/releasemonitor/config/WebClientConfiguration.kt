package com.github.jntakpe.releasemonitor.config

import com.github.jntakpe.releasemonitor.config.properties.ArtifactoryProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@Configuration
class WebClientConfiguration(private val artifactoryProperties: ArtifactoryProperties) {

    companion object {
        private val AUTHORIZATION_HEADER = "Authorization"
        private val BASE_API = "/artifactory/api"
        private val STORAGE_API = "/storage"
    }

    @Bean
    fun artifactoryClient() = WebClient.builder().baseUrl(baseUrl()).defaultHeader(AUTHORIZATION_HEADER, buildBasicHeaderValue()).build()

    private fun baseUrl() = artifactoryProperties.host + BASE_API + STORAGE_API + artifactoryProperties.gradleRepository

    private fun buildBasicHeaderValue(): String {
        val credentials = artifactoryProperties.credentials
        val concat = "${credentials.username}:${credentials.password}"
        return "Basic ${Base64.getEncoder().encodeToString(concat.toByteArray())}"
    }

}