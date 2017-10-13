package com.github.jntakpe.releasemonitor.repository

import com.github.jntakpe.releasemonitor.model.Application
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
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

    @get:Rule
    val wireMockRule = WireMockRule(8089)

    @Autowired
    private lateinit var artifactoryRepository: ArtifactoryRepository

    @Test
    fun `findVersions should retrieve versions`() {
        val app = Application("release-monitor", "com.github.jntakpe")
        artifactoryRepository.findVersions(app).test()
                .consumeNextWith { assertThat(it).isNotEmpty }
                .verifyComplete()
    }

    @Test
    fun `findVersions should fail cuz unknown application`() {
        val app = Application("service-unknown", "com.github.jntakpe")
        artifactoryRepository.findVersions(app).test()
                .verifyError(WebClientResponseException::class.java)
    }

    @Test
    fun `findVersions should filter maven metadata`() {
        val app = Application("release-monitor", "com.github.jntakpe")
        artifactoryRepository.findVersions(app).test()
                .consumeNextWith { assertThat(it).doesNotContain("maven-metadata.xml") }
                .verifyComplete()
    }

}