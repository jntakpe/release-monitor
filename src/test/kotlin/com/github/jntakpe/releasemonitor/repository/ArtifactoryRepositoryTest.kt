package com.github.jntakpe.releasemonitor.repository

import com.github.jntakpe.releasemonitor.model.Application
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.test.test

@SpringBootTest
@RunWith(SpringRunner::class)
class ArtifactoryRepositoryTest {

    @Autowired private lateinit var artifactoryRepository: ArtifactoryRepository

    @Test
    fun `findVersions should retrieve versions`() {
        val versionsSizeWithoutMavenMetadata = 8L - 1
        val app = Application("com.github.jntakpe", "release-monitor")
        artifactoryRepository.findVersions(app).test()
                .recordWith { ArrayList() }
                .expectNextCount(versionsSizeWithoutMavenMetadata)
                .consumeRecordedWith { assertThat(it.map { it.raw }).contains("0.1.0-RC1", "0.1.0-SNAPSHOT") }
                .verifyComplete()
    }

    @Test
    fun `findVersions should fail cuz unknown application`() {
        val app = Application("com.github.jntakpe", "service-unknown")
        artifactoryRepository.findVersions(app).test()
                .verifyError(WebClientResponseException::class.java)
    }

    @Test
    fun `findVersions should retrieve versions sorted`() {
        val versionsSizeWithoutMavenMetadata = 8L - 1
        val app = Application("com.github.jntakpe", "release-monitor")
        artifactoryRepository.findVersions(app).test()
                .recordWith { ArrayList() }
                .expectNextCount(versionsSizeWithoutMavenMetadata)
                .consumeRecordedWith { assertThat(ArrayList(it)).isSorted() }
                .verifyComplete()
    }

}