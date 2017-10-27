package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.model.Environment
import com.github.jntakpe.releasemonitor.repository.EnvironmentRepository
import com.github.jntakpe.releasemonitor.utils.loggerFor
import org.bson.types.ObjectId
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Service
class EnvironmentService(private val environmentRepository: EnvironmentRepository) {

    companion object {
        private val LOGGER = loggerFor<EnvironmentService>()
    }

    fun create(env: Environment): Mono<Environment> {
        return Mono.just(env)
                .doOnNext { LOGGER.info("Creating $it") }
                .flatMap { environmentRepository.save(it) }
                .doOnSuccess { LOGGER.info("$it created") }
    }

    fun update(id: ObjectId, env: Environment): Mono<Environment> {
        return findById(id)
                .map { checkIdMatches(id, env) }
                .switchIfEmpty(errorIfEmpty(id))
                .doOnNext { EnvironmentService.LOGGER.info("Updating $it") }
                .flatMap { environmentRepository.save(it) }
                .doOnSuccess { EnvironmentService.LOGGER.info("$it updated") }
    }

    private fun findById(id: ObjectId): Mono<Environment> {
        return Mono.just(id)
                .doOnNext { EnvironmentService.LOGGER.debug("Searching environment with id $it") }
                .flatMap { environmentRepository.findById(it) }
                .doOnNext { EnvironmentService.LOGGER.debug("$it retrieved with id $id") }
    }

    private fun errorIfEmpty(id: ObjectId): Mono<Environment> {
        return EmptyResultDataAccessException("Unable to find environment matching id $id", 1).toMono<Environment>()
                .doOnError { EnvironmentService.LOGGER.warn(it.message) }
    }

    private fun checkIdMatches(id: ObjectId, env: Environment): Environment {
        assert(env.id == null || env.id == id, { "Id $id doesn't match environment's $env" })
        return env.copy(id = id)
    }
}