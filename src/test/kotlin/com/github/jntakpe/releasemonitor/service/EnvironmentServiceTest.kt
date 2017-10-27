package com.github.jntakpe.releasemonitor.service

import com.github.jntakpe.releasemonitor.dao.EnvironmentDAO
import com.github.jntakpe.releasemonitor.model.Environment
import com.github.jntakpe.releasemonitor.model.EnvironmentType
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.Before
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

    @Test
    fun `update should update existing`() {
        val initCount = environmentDAO.count()
        val env = environmentDAO.findAny()
        val updatedName = "updated"
        environmentService.update(env.id!!, env.copy(name = updatedName)).test()
                .consumeNextWith {
                    Assertions.assertThat(it.id).isEqualTo(env.id)
                    Assertions.assertThat(it.name).isEqualTo(updatedName)
                    Assertions.assertThat(environmentDAO.count()).isEqualTo(initCount)
                }
                .verifyComplete()
    }

    @Test
    fun `update should fail if id missing`() {
        environmentService.update(ObjectId(), environmentDAO.findAny()).test()
                .verifyError(EmptyResultDataAccessException::class.java)
    }

    @Test
    fun `update should fail if id does not match`() {
        val env = environmentDAO.findAny()
        environmentService.update(env.id!!, env.copy(id = ObjectId(), name = "updated")).test()
                .verifyError(AssertionError::class.java)
    }

    @Test
    fun `update should set id if null`() {
        val env = environmentDAO.findAny()
        environmentService.update(env.id!!, env.copy(id = null, name = "updated")).test()
                .consumeNextWith { a -> Assertions.assertThat(a.id).isEqualTo(env.id) }
                .verifyComplete()
    }
}