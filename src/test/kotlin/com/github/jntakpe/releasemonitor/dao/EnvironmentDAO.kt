package com.github.jntakpe.releasemonitor.dao

import com.github.jntakpe.releasemonitor.model.Environment
import com.github.jntakpe.releasemonitor.model.EnvironmentType
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class EnvironmentDAO(private val template: MongoTemplate) {

    fun count() = template.count(Query(), Environment::class.java)

    fun deleteAll() = template.remove(Query(), Environment::class.java)

    fun insertAll() = template.insertAll(listOf(createDxpAssembly(), createDxpIntegration()))

    fun createDxpAssembly() = Environment("dxp-azure", EnvironmentType.ASSEMBLY, "http://dxpasm.edgility.cloud")

    fun createDxpIntegration() = Environment("dxp-azure", EnvironmentType.INTEGRATION, "http://dxpint.edgility.cloud")

    fun findAny() = template.find(Query(), Environment::class.java).firstOrNull() ?: throw IllegalStateException("No env found")

}