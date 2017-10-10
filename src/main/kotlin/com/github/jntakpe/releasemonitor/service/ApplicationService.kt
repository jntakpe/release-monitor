package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.repository.ApplicationRepository
import com.github.jntakpe.releasemonitor.utils.loggerFor
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ApplicationService(private val applicationRepository: ApplicationRepository) {

    companion object {
        private val LOGGER = loggerFor<ApplicationService>()
    }

    fun create(application: Application): Mono<Application> {
        LOGGER.info("Creating {}", application)
        return applicationRepository.save(application)
                .doOnSuccess { LOGGER.info("{} created", it) }
    }

}