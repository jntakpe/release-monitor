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
        private val STORAGE_API = "/storage"
        private val GRADLE_REPO = "/gradle-repo-local"
        private val MAVEN_METADATA = "maven-metadata.xml"
        private val LOGGER = loggerFor<ArtifactoryRepository>()
    }

    fun findVersions(app: Application): Flux<AppVersion> {
        LOGGER.debug("Searching $app versions")
        return artifactoryClient.get().uri(createFolderPath(app)).retrieve()
                .bodyToMono(Folder::class.java)
                .map { it.toRawVersions() }
                .flatMapMany { Flux.fromIterable(it) }
                .filter { !isMavenMetadata(it) }
                .map { it.toAppVersion() }
                .doOnNext { LOGGER.debug("Versions $it updated") }
    }

    private fun createFolderPath(app: Application) = "$STORAGE_API$GRADLE_REPO/${app.group.dotToSlash()}/${app.name}"


    private fun isMavenMetadata(input: String) = MAVEN_METADATA == input
}