package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.model.AppVersion
import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.repository.ApplicationRepository
import com.github.jntakpe.releasemonitor.repository.ArtifactoryRepository
import com.github.jntakpe.releasemonitor.utils.loggerFor
import org.bson.types.ObjectId
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.stream.Collectors

@Service
class ApplicationService(private val applicationRepository: ApplicationRepository, private val artifactoryRepository: ArtifactoryRepository) {

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
                .map { checkIdMatches(id, app) }
                .switchIfEmpty(errorIfEmpty(id))
                .doOnNext { LOGGER.info("Updating $it") }
                .flatMap { applicationRepository.save(it) }
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

    fun refreshVersions(app: Application): Mono<Application> {
        return artifactoryRepository.findVersions(app)
                .collect(Collectors.toList())
                .onErrorResume({ isNotFoundError(it) }, { Mono.just(emptyList()) })
                .flatMap { updateVersionsIfNeeded(app, it) }
    }

    private fun updateVersionsIfNeeded(existing: Application, versions: List<AppVersion>): Mono<Application> {
        return if (existing.versions == versions) {
            Mono.just(existing)
        } else {
            LOGGER.info("Update $existing versions to $versions")
            applicationRepository.save(existing.copy(versions = versions))
                    .doOnSuccess { LOGGER.info("$it versions updated") }
        }
    }

    private fun isNotFoundError(e: Throwable) = e is WebClientResponseException && e.statusCode == HttpStatus.NOT_FOUND

    private fun findById(id: ObjectId): Mono<Application> {
        return Mono.just(id)
                .doOnNext { LOGGER.debug("Searching application with id $it") }
                .flatMap { applicationRepository.findById(it) }
                .doOnNext { LOGGER.debug("$it retrieved with id $id") }
    }

    private fun errorIfEmpty(id: ObjectId): Mono<Application> {
        return EmptyResultDataAccessException("Unable to find application matching id $id", 1).toMono<Application>()
                .doOnError { LOGGER.warn(it.message) }
    }

    private fun checkIdMatches(id: ObjectId, app: Application): Application {
        assert(app.id == null || app.id == id, { "Id $id doesn't match application's $app" })
        return app.copy(id = id)
    }

}