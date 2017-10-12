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

    fun create(application: Application): Mono<Application> {
        LOGGER.info("Creating {}", application).toMono()
        return applicationRepository.save(application)
                .doOnSuccess { LOGGER.info("{} created", it) }
    }

    fun findAll(): Flux<Application> {
        LOGGER.debug("Searching all applications")
        return applicationRepository.findAll()
                .doOnComplete { LOGGER.debug("All applications retrieved") }
    }

}