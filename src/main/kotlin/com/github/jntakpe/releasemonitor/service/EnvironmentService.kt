package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.model.Environment
import com.github.jntakpe.releasemonitor.repository.EnvironmentRepository
import com.github.jntakpe.releasemonitor.utils.loggerFor
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class EnvironmentService(private val environmentRepository: EnvironmentRepository) {

    companion object {
        private val LOGGER = loggerFor<ApplicationService>()
    }

    fun create(env: Environment): Mono<Environment> {
        return Mono.just(env)
                .doOnNext { LOGGER.info("Creating $it") }
                .flatMap { environmentRepository.save(it) }
                .doOnSuccess { LOGGER.info("$it created") }
    }
}