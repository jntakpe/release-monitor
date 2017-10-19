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
        return Mono.just(app)
                .doOnNext { LOGGER.info("Creating $it") }
                .flatMap { applicationRepository.save(it) }
                .doOnSuccess { LOGGER.info("$it created") }
    }

    fun update(id: ObjectId, app: Application): Mono<Application> {
        return findById(id)
                .switchIfEmpty(errorIfEmpty(id))
                .doOnNext { LOGGER.info("Updating $app") }
                .flatMap { applicationRepository.save(app) }
                .doOnSuccess { LOGGER.info("$it updated") }
    }

    fun delete(id: ObjectId): Mono<Application> {
        return findById(id)
                .switchIfEmpty(errorIfEmpty(id))
                .doOnNext { LOGGER.info("Deleting $it") }
                .flatMap { a -> applicationRepository.delete(a).then(a.toMono()) }
                .doOnSuccess { LOGGER.info("$it deleted") }
    }

    fun findAll(): Flux<Application> {
        return Mono.just(true)
                .doOnNext { LOGGER.debug("Searching all applications") }
                .flatMapMany { applicationRepository.findAll() }
                .doOnComplete { LOGGER.debug("All applications retrieved") }
    }

    private fun findById(id: ObjectId): Mono<Application> {
        return Mono.just(id)
                .doOnNext { LOGGER.debug("Searching application with id $it") }
                .flatMap { applicationRepository.findById(it) }
                .doOnNext { LOGGER.debug("$it retrieved with id $id") }
    }

    private fun errorIfEmpty(id: ObjectId): Mono<Application> {
        return Mono.error<Application>(EmptyResultDataAccessException("Unable to find application matching id $id", 1))
                .doOnError { LOGGER.warn(it.message) }
    }

}