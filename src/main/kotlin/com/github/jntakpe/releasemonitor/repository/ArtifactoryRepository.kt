package com.github.jntakpe.releasemonitor.repository

import com.github.jntakpe.releasemonitor.mapper.toAppVersion
import com.github.jntakpe.releasemonitor.mapper.toRawVersions
import com.github.jntakpe.releasemonitor.model.AppVersion
import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.model.client.Folder
import com.github.jntakpe.releasemonitor.utils.dotToSlash
import com.github.jntakpe.releasemonitor.utils.loggerFor
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Repository
class ArtifactoryRepository(private val artifactoryClient: WebClient) {

    companion object {
        private val MAVEN_METADATA = "maven-metadata.xml"
        private val LOGGER = loggerFor<ArtifactoryRepository>()
    }

    fun findVersions(app: Application): Flux<AppVersion> {
        LOGGER.debug("Searching $app versions")
        return findRawVersions(app)
                .map { it.toAppVersion() }
                .sort()
                .doOnNext { LOGGER.debug("Versions $it updated") }
    }

    private fun findRawVersions(app: Application): Flux<String> {
        return artifactoryClient.get().uri(folderPath(app)).retrieve()
                .bodyToMono(Folder::class.java)
                .map { it.toRawVersions() }
                .flatMapMany { Flux.fromIterable(it) }
                .filter { !isMavenMetadata(it) }
    }

    private fun folderPath(app: Application) = "/${app.group.dotToSlash()}/${app.name}"

    private fun isMavenMetadata(input: String) = MAVEN_METADATA == input
}