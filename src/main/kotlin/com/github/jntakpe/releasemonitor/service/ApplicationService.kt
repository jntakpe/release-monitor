package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.repository.ApplicationRepository
import com.github.jntakpe.releasemonitor.utils.loggerFor
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Service
class ApplicationService(private val applicationRepository: ApplicationRepository) {

    companion object {
        private val LOGGER = loggerFor<ApplicationService>()
    }

    fun create(app: Application): Mono<Application> {
        LOGGER.info("Creating $app").toMono()
        return applicationRepository.save(app)
                .doOnSuccess { LOGGER.info("$it created") }
    }

    fun findAll(): Flux<Application> {
        LOGGER.debug("Searching all applications")
        return applicationRepository.findAll()
                .doOnComplete { LOGGER.debug("All applications retrieved") }
    }

}