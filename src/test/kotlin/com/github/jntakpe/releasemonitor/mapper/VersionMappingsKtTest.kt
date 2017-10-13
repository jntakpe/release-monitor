package com.github.jntakpe.releasemonitor.mapper

import com.github.jntakpe.releasemonitor.model.client.Folder
import com.github.jntakpe.releasemonitor.model.client.FolderChildren
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class VersionMappingsKtTest {

    @Test
    fun `toVersions should map folder to version list`() {
        val folder = Folder(listOf(FolderChildren("/1.0.0-RC1"), FolderChildren("/1.0.0")))
        assertThat(folder.toVersions()).containsExactly("1.0.0-RC1", "1.0.0")
    }
}