package com.github.jntakpe.releasemonitor.web

import com.github.jntakpe.releasemonitor.dao.ApplicationDAO
import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.model.api.ApplicationDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationHandlerTest {

    @LocalServerPort private var port: Int? = null

    @Autowired private lateinit var applicationDAO: ApplicationDAO

    private lateinit var client: WebTestClient

    @Before
    fun setup() {
        applicationDAO.deleteAll()
        applicationDAO.insertAll()
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:$port")
                .build()
    }

    @Test
    fun `create should create a new application`() {
        val input = Application("foo", "bar")
        client.post()
                .uri("$API$APPLICATIONS")
                .syncBody(input)
                .exchange()
                .expectStatus().isCreated
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(ApplicationDTO::class.java).consumeWith<Nothing?> {
            val app = it.responseBody
            assertThat(app?.id).isNotNull()
            assertThat(app).isEqualToIgnoringGivenFields(input, ApplicationDTO::id.name)
        }
    }

    @Test
    fun `create should fail cuz application exists`() {
        client.post()
                .uri("$API$APPLICATIONS")
                .syncBody(applicationDAO.createMockPi())
                .exchange()
                .expectStatus().is5xxServerError
    }
}