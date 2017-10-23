package com.github.jntakpe.releasemonitor.web

import com.github.jntakpe.releasemonitor.mapper.toDTO
import com.github.jntakpe.releasemonitor.mapper.toEntity
import com.github.jntakpe.releasemonitor.model.api.ApplicationDTO
import com.github.jntakpe.releasemonitor.service.ApplicationService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.net.URI

@Component
class ApplicationHandler(private val applicationService: ApplicationService) {

    fun findAll(request: ServerRequest) = ServerResponse.ok().body(applicationService.findAll().map { it.toDTO() })

    fun monitor(request: ServerRequest) = ServerResponse.ok().bodyToServerSentEvents(applicationService.monitor().map { it.toDTO() })

    fun create(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(ApplicationDTO::class.java)
                .map { it.toEntity() }
                .flatMap { applicationService.create(it) }
                .map { it.toDTO() }
                .flatMap { ServerResponse.created(URI.create("$API$APPLICATIONS/${it.id}")).syncBody(it) }
    }

    fun update(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(ApplicationDTO::class.java)
                .map { it.toEntity() }
                .map { request.idFromPath() to it }
                .flatMap { (id, app) -> applicationService.update(id, app) }
                .map { it.toDTO() }
                .flatMap { ServerResponse.ok().syncBody(it) }
    }

    fun delete(request: ServerRequest): Mono<ServerResponse> {
        return request.idFromPath().toMono()
                .flatMap { applicationService.delete(it) }
                .flatMap { ServerResponse.noContent().build() }
    }

}