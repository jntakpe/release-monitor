package com.github.jntakpe.releasemonitor.dao

import com.github.jntakpe.releasemonitor.model.Application
import org.bson.types.ObjectId
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

    fun findAny() = template.find(Query(), Application::class.java).firstOrNull() ?: throw IllegalStateException("No app found")

    fun findAll() = template.findAll(Application::class.java)

    fun findById(id: ObjectId) = template.findById(id, Application::class.java)
}