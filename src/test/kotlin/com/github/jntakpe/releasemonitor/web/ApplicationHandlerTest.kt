package com.github.jntakpe.releasemonitor.web

import com.github.jntakpe.releasemonitor.dao.ApplicationDAO
import com.github.jntakpe.releasemonitor.mapper.toDTO
import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.model.api.ApplicationDTO
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
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
    fun `find all should find all`() {
        client.get()
                .uri("$API$APPLICATIONS")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(ApplicationDTO::class.java).consumeWith<Nothing?> {
            val apps = it.responseBody
            assertThat(apps).isNotEmpty
            assertThat(apps).hasSize(applicationDAO.count().toInt())
        }
    }

    @Test
    fun `find all should find none`() {
        applicationDAO.deleteAll()
        client.get()
                .uri("$API$APPLICATIONS")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(ApplicationDTO::class.java).consumeWith<Nothing?> {
            val apps = it.responseBody
            assertThat(apps).isEmpty()
        }
    }

    @Test
    fun `create should create a new application`() {
        val input = Application("foo", "bar")
        val initCount = applicationDAO.count()
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
            assertThat(applicationDAO.count()).isEqualTo(initCount + 1)
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

    @Test
    fun `update should update existing`() {
        val name = "updated"
        val input = applicationDAO.findAny().copy(name = name).toDTO()
        client.put()
                .uri("$API$APPLICATIONS/{id}", input.id)
                .syncBody(input)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(ApplicationDTO::class.java).consumeWith<Nothing?> {
            val app = it.responseBody
            assertThat(app).isNotNull()
            assertThat(app.let { name }).isEqualTo(name)
            assertThat(app).isEqualToIgnoringGivenFields(input, "name")
            assertThat(applicationDAO.findById(ObjectId(input.id)).name).isEqualTo(name)
        }
    }

    @Test
    fun `update should fail if wrong id`() {
        client.put()
                .uri("$API$APPLICATIONS/{id}", ObjectId())
                .syncBody(applicationDAO.findAny().toDTO())
                .exchange()
                .expectStatus().is5xxServerError
    }

    @Test
    fun `delete should delete`() {
        val initCount = applicationDAO.count()
        val id = applicationDAO.findAny().id.toString()
        client.delete()
                .uri("$API$APPLICATIONS/{id}", id)
                .exchange()
                .expectStatus().isNoContent
                .expectBody().consumeWith { assertThat(applicationDAO.count()).isEqualTo(initCount - 1) }
    }

}