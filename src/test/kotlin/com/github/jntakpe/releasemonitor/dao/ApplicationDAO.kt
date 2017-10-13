package com.github.jntakpe.releasemonitor.dao

import com.github.jntakpe.releasemonitor.model.Application
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class ApplicationDAO(private val template: MongoTemplate) {

    fun count() = template.count(Query(), Application::class.java)

    fun deleteAll() = template.remove(Query(), Application::class.java)

    fun insertAll() = template.insertAll(listOf(createMockPi(), createSpringBoot()))

    fun createMockPi() = Application("com.github.jntakpe", "mockpi")

    fun createSpringBoot() = Application("com.springframework.boot", "spring-boot")

}