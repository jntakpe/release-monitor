package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.dao.ApplicationDAO
import com.github.jntakpe.releasemonitor.model.Application
import com.github.tomakehurst.wiremock.WireMockServer
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.test

@SpringBootTest
@RunWith(SpringRunner::class)
class ApplicationServiceTest {

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

    @Autowired lateinit private var applicationService: ApplicationService

    @Autowired lateinit private var applicationDAO: ApplicationDAO

    @Before
    fun setUp() {
        applicationDAO.deleteAll()
        applicationDAO.insertAll()
    }

    @Test
    fun `create should create a new application`() {
        val initCount = applicationDAO.count()
        val application = Application("bar", "foo")
        applicationService.create(application).test()
                .consumeNextWith {
                    assertThat(it.id).isNotNull()
                    assertThat(it).isEqualTo(application)
                    assertThat(applicationDAO.count()).isEqualTo(initCount + 1)
                }
                .verifyComplete()
    }

    @Test
    fun `create should fail cuz application exists`() {
        applicationService.create(applicationDAO.createMockPi()).test()
                .verifyError(DuplicateKeyException::class.java)
    }

    @Test
    fun `update should update existing`() {
        val initCount = applicationDAO.count()
        val app = applicationDAO.findAny()
        val updatedName = "updated"
        applicationService.update(app.id!!, app.copy(name = updatedName)).test()
                .consumeNextWith {
                    assertThat(it.id).isEqualTo(app.id)
                    assertThat(it.name).isEqualTo(updatedName)
                    assertThat(applicationDAO.count()).isEqualTo(initCount)
                }
                .verifyComplete()
    }

    @Test
    fun `update should fail if id missing`() {
        applicationService.update(ObjectId(), applicationDAO.findAny()).test()
                .verifyError(EmptyResultDataAccessException::class.java)
    }

    @Test
    fun `update should fail if id does not match`() {
        val app = applicationDAO.findAny()
        applicationService.update(app.id!!, app.copy(id = ObjectId(), name = "updated")).test()
                .verifyError(AssertionError::class.java)
    }

    @Test
    fun `update should set id if null`() {
        val app = applicationDAO.findAny()
        applicationService.update(app.id!!, app.copy(id = null, name = "updated")).test()
                .consumeNextWith { a -> assertThat(a.id).isEqualTo(app.id) }
                .verifyComplete()
    }

    @Test
    fun `delete should remove application`() {
        val initCount = applicationDAO.count()
        val app = applicationDAO.findAny()
        applicationService.delete(app.id!!).test()
                .consumeNextWith {
                    assertThat(applicationDAO.count()).isEqualTo(initCount - 1)
                    assertThat(applicationDAO.findAll()).doesNotContain(app)
                }
                .verifyComplete()
    }

    @Test
    fun `delete should fail if id missing`() {
        applicationService.delete(ObjectId()).test()
                .verifyError(EmptyResultDataAccessException::class.java)
    }

    @Test
    fun `find all should retrieve some`() {
        applicationService.findAll().test()
                .recordWith { ArrayList() }
                .expectNextCount(applicationDAO.count())
                .consumeRecordedWith { assertThat(it).contains(applicationDAO.createMockPi(), applicationDAO.createReleaseMonitor()) }
                .verifyComplete()
    }

    @Test
    fun `find all should be empty`() {
        applicationDAO.deleteAll()
        applicationService.findAll().test()
                .expectNextCount(0)
                .verifyComplete()
    }

    @Test
    fun `monitor should retrieve some with versions`() {
        val count = applicationDAO.count()
        applicationService.monitor().test()
                .recordWith { ArrayList() }
                .expectNextCount(count)
                .consumeRecordedWith {
                    assertThat(it).hasSize(count.toInt())
                    it.map { it.versions }.forEach { assertThat(it).isNotEmpty }
                }
                .verifyComplete()
    }

    @Test
    fun `monitor should update versions`() {
        applicationService.monitor().test()
                .recordWith { ArrayList() }
                .expectNextCount(applicationDAO.count())
                .consumeRecordedWith {
                    assertThat(it).isNotEmpty
                    it.forEach { assertThat(it.versions).isEqualTo(applicationDAO.findById(it.id!!).versions) }
                }
                .verifyComplete()
    }
}