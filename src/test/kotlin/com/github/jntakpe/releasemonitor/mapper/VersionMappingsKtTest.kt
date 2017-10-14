package com.github.jntakpe.releasemonitor.mapper

import com.github.jntakpe.releasemonitor.model.AppVersion
import com.github.jntakpe.releasemonitor.model.VersionType
import com.github.jntakpe.releasemonitor.model.client.Folder
import com.github.jntakpe.releasemonitor.model.client.FolderChildren
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class VersionMappingsKtTest {

    @Test
    fun `toRawVersions should map folder to raw version list`() {
        val folder = Folder(listOf(FolderChildren("/1.0.0-RC1"), FolderChildren("/1.0.0")))
        assertThat(folder.toRawVersions()).containsExactly("1.0.0-RC1", "1.0.0")
    }

    @Test
    fun `toAppVersion should map raw string to release`() {
        val raw = "1.2.3"
        assertThat(raw.toAppVersion()).isEqualToComparingFieldByField(AppVersion(raw, 1, 2, 3, VersionType.RELEASE))
    }

    @Test
    fun `toAppVersion should map raw string to release with suffix`() {
        val raw = "1.2.3-RELEASE"
        assertThat(raw.toAppVersion()).isEqualToComparingFieldByField(AppVersion(raw, 1, 2, 3, VersionType.RELEASE))
    }

    @Test
    fun `toAppVersion should map raw string to release candidate`() {
        val raw = "1.2.3-RC1"
        assertThat(raw.toAppVersion()).isEqualToComparingFieldByField(AppVersion(raw, 1, 2, 3, VersionType.RELEASE_CANDIDATE, 1))
    }

    @Test
    fun `toAppVersion should map raw string to snapshot`() {
        val raw = "0.1.0-SNAPSHOT"
        assertThat(raw.toAppVersion()).isEqualToComparingFieldByField(AppVersion(raw, 0, 1, 0, VersionType.SNAPSHOT))
    }
}