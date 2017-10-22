package com.github.jntakpe.releasemonitor.dao

import com.github.jntakpe.releasemonitor.model.AppVersion
import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.model.VersionType
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class ApplicationDAO(private val template: MongoTemplate) {

    fun count() = template.count(Query(), Application::class.java)

    fun deleteAll() = template.remove(Query(), Application::class.java)

    fun insertAll() = template.insertAll(listOf(createMockPi(), createReleaseMonitor()))

    fun createMockPi() = Application("com.github.jntakpe", "mockpi")

    fun createReleaseMonitor() = Application("com.github.jntakpe", "release-monitor", listOf(version()))

    fun findAny() = template.find(Query(), Application::class.java).firstOrNull() ?: throw IllegalStateException("No app found")

    fun findAll() = template.findAll(Application::class.java)

    fun findById(id: ObjectId) = template.findById(id, Application::class.java)

    private fun version() = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
}