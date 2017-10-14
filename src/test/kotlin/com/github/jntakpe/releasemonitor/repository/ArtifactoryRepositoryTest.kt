package com.github.jntakpe.releasemonitor.repository

import com.github.jntakpe.releasemonitor.model.Application
import com.github.tomakehurst.wiremock.WireMockServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
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

    companion object {
        private val wiremockServer = WireMockServer(8089)

        @JvmStatic
        @BeforeClass
        fun setup() {
            wiremockServer.start()
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            wiremockServer.stop()
        }
    }

    @Autowired
    private lateinit var artifactoryRepository: ArtifactoryRepository

    @Test
    fun `findVersions should retrieve versions`() {
        val initVersionsSize = 8L
        val versionsSizeWithoutMavenMetadata = initVersionsSize - 1
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

}