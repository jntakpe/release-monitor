package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.dao.EnvironmentDAO
import com.github.jntakpe.releasemonitor.model.Environment
import com.github.jntakpe.releasemonitor.model.EnvironmentType
import org.assertj.core.api.Assertions
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
class EnvironmentServiceTest {

    @Autowired lateinit private var environmentService: EnvironmentService

    @Autowired lateinit private var environmentDAO: EnvironmentDAO

    @Before
    fun setup() {
        environmentDAO.deleteAll()
        environmentDAO.insertAll()
    }

    @Test
    fun `create should create a new environment`() {
        val initCount = environmentDAO.count()
        val environment = Environment("bar", EnvironmentType.ASSEMBLY, "foo")
        environmentService.create(environment).test()
                .consumeNextWith {
                    Assertions.assertThat(it.id).isNotNull()
                    Assertions.assertThat(it).isEqualTo(environment)
                    Assertions.assertThat(environmentDAO.count()).isEqualTo(initCount + 1)
                }
                .verifyComplete()
    }

    @Test
    fun `create should fail cuz environment exists`() {
        environmentService.create(environmentDAO.createDxpAssembly()).test()
                .verifyError(DuplicateKeyException::class.java)
    }
}