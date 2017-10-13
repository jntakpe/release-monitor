package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.repository.ApplicationRepository
import com.github.jntakpe.releasemonitor.utils.loggerFor
import org.bson.types.ObjectId
import org.springframework.dao.EmptyResultDataAccessException
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

    fun update(id: ObjectId, app: Application): Mono<Application> {
        LOGGER.info("Updating $app")
        return findById(id)
                .switchIfEmpty(errorIfEmpty(id))
                .flatMap { applicationRepository.save(app) }
                .doOnSuccess { LOGGER.info("$it updated") }
    }

    fun findAll(): Flux<Application> {
        LOGGER.debug("Searching all applications")
        return applicationRepository.findAll()
                .doOnComplete { LOGGER.debug("All applications retrieved") }
    }

    private fun findById(id: ObjectId): Mono<Application> {
        LOGGER.debug("Searching application with id {}", id)
        return applicationRepository.findById(id)
                .doOnSuccess { LOGGER.debug("$it retrieved with id $id") }
    }

    private fun errorIfEmpty(id: ObjectId): Mono<Application> {
        val message = "Unable to find application matching id $id"
        LOGGER.warn(message)
        return Mono.error(EmptyResultDataAccessException(message, 1))
    }

}