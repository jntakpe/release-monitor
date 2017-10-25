package com.github.jntakpe.releasemonitor.config

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Component
class ConfigListener {

    companion object {
        private val wireMockServer = WireMockServer(8089)
        private val startLock = ReentrantLock()
        private val stopLock = ReentrantLock()
    }

    @EventListener
    fun handleContextRefresh(event: ApplicationReadyEvent) {
        startLock.run { if (!isWiremockRunning()) wireMockServer.start() }
    }

    @EventListener
    fun handleContextRefresh(event: ContextClosedEvent) {
        stopLock.run { if (isWiremockRunning()) wireMockServer.stop() }
    }

    private fun isWiremockRunning() = wireMockServer.isRunning

    private fun Lock.run(body: () -> Unit) {
        lock()
        try {
            body()
        } finally {
            unlock()
        }
    }

}