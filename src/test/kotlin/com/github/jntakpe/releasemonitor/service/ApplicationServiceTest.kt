package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.dao.ApplicationDAO
import com.github.jntakpe.releasemonitor.model.Application
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.test

@SpringBootTest
@RunWith(SpringRunner::class)
class ApplicationServiceTest {

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
        val application = Application("foo", "bar")
        applicationService.create(application).test()
                .consumeNextWith {
                    assertThat(it.id).isNotNull()
                    assertThat(it).isEqualTo(application)
                    assertThat(initCount).isEqualTo(applicationDAO.count())
                }
    }

    @Test
    fun `create should fail cuz application exists`() {
        applicationService.create(applicationDAO.createMockPi()).test()
                .expectError(DuplicateKeyException::class.java)
    }

    @Test
    fun `find all should retrieve some`() {
        applicationService.findAll().test()
                .recordWith { ArrayList() }
                .expectNextCount(applicationDAO.count())
                .consumeRecordedWith { assertThat(it).contains(applicationDAO.createMockPi(), applicationDAO.createSpringBoot()) }
                .verifyComplete()
    }

    @Test
    fun `find all should be empty`() {
        applicationDAO.deleteAll()
        applicationService.findAll().test()
                .expectNextCount(0)
                .verifyComplete()
    }
}