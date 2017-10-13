package com.github.jntakpe.releasemonitor.repository

import com.github.jntakpe.releasemonitor.mapper.toVersions
import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.model.client.Folder
import com.github.jntakpe.releasemonitor.utils.dotToSlash
import com.github.jntakpe.releasemonitor.utils.loggerFor
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Repository
class ArtifactoryRepository(private val artifactoryClient: WebClient) {

    companion object {
        private val STORAGE_API = "/storage"
        private val GRADLE_REPO = "/gradle-repo-local"
        private val MAVEN_METADATA = "maven-metadata.xml"
        private val LOGGER = loggerFor<ArtifactoryRepository>()
    }

    fun findVersions(app: Application): Mono<List<String>> {
        LOGGER.debug("Searching $app versions")
        println(createFolderPath(app))
        return artifactoryClient.get().uri(createFolderPath(app)).retrieve()
                .bodyToMono(Folder::class.java)
                .map { it.toVersions() }
                .map { removeMavenMetadata(it) }
                .doOnNext { LOGGER.debug("Versions $it updated") }
    }

    private fun createFolderPath(app: Application) = "$STORAGE_API$GRADLE_REPO/${app.group.dotToSlash()}/${app.name}"

    private fun removeMavenMetadata(versions: List<String>) = versions.filter { !isMavenMetadata(it) }

    private fun isMavenMetadata(input: String) = MAVEN_METADATA == input
}