package com.github.jntakpe.releasemonitor.config

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ConfigListener {

    companion object {
        private val wireMockServer = WireMockServer(8089)
    }

    @EventListener
    fun handleContextRefresh(event: ApplicationReadyEvent) {
        if (!isWiremockRunning()) {
            wireMockServer.start()
        }
    }

    @EventListener
    fun handleContextRefresh(event: ContextClosedEvent) {
        if (isWiremockRunning()) {
            wireMockServer.stop()
        }
    }

    private fun isWiremockRunning() = wireMockServer.isRunning

}