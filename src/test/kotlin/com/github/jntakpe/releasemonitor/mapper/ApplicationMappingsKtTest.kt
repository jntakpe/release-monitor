package com.github.jntakpe.releasemonitor.mapper

import com.github.jntakpe.releasemonitor.model.AppVersion
import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.model.VersionType
import com.github.jntakpe.releasemonitor.model.api.ApplicationDTO
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Test

class ApplicationMappingsKtTest {

    @Test
    fun `map should map dto without id to entity`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        val dto = ApplicationDTO("foo", "bar", listOf(version))
        val entity = dto.toEntity()
        assertThat(entity).isNotNull()
        assertThat(entity.id).isNull()
        assertThat(entity.group).isEqualTo(dto.group)
        assertThat(entity.name).isEqualTo(dto.name)
        assertThat(entity.versions).isEqualTo(dto.versions)
    }

    @Test
    fun `map should map dto with id to entity`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        val id = ObjectId().toString()
        val entity = ApplicationDTO("foo", "bar", listOf(version), id).toEntity()
        assertThat(entity.id).isEqualTo(ObjectId(id))
    }

    @Test
    fun `map should map entity without id to DTO`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        val entity = Application("foo", "bar", listOf(version))
        val dto = entity.toDTO()
        assertThat(dto).isNotNull()
        assertThat(dto.group).isEqualTo(entity.group)
        assertThat(dto.name).isEqualTo(entity.name)
        assertThat(dto.versions).isEqualTo(entity.versions)
    }

    @Test
    fun `map should map entity with id to DTO`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        val id = ObjectId()
        val entity = Application("foo", "bar", listOf(version), id)
        val dto = entity.toDTO()
        assertThat(dto.id).isEqualTo(id.toString())
    }

}